import java.util.*;
import java.io.*;

public class TwoPass {
	
	
	public static void main(String[] args) throws IOException {
		

		
	/// FIRST PASS STARTS FROM HERE ///////////////
		//Scanner input = new Scanner(new File(args[0]));
		
		
		
		// in this memoryMap, I will store calculated addresses and external references symbols
		
		ArrayList<String> memoryMap = new ArrayList<String>();
		ArrayList<Uses> usedList = new ArrayList<Uses>();
		
		// in this arraylist, I will store symbol object each time I see them
		ArrayList<Definition> symbolTable = new ArrayList<Definition>();
		
		ArrayList<Integer> multiplyUsedError = new ArrayList<Integer>();
		
		
		// this will be incremented by the number of previous module's instructions
		// each time the encapsulating for loop index i is incremented
		
		int moduleBaseAddrs = 0;
		int numInstruction = 0; 
		
		
		Scanner input = new Scanner(System.in);

		String inputVal = "";
		//StringBuilder inputVal = new StringBuilder();
		
		
		while (input.hasNext()) {
			
			int moduleNum = input.nextInt();
			//////////////MODULE////////////////////////
			
			for (int i = 0; i < moduleNum; i++) {
				moduleBaseAddrs += numInstruction;
				
				
				
				// we'll be looping by this amount to
				// store the definition spotted into definition arrayList
				int numDef = input.nextInt();
				
				
				///////////////////////////////////////////////////
				
				
				//////////DEFINITION LIST//////////////////////////
				// loop by the number of definition pairs given in the module
				//boolean justAdded = false;
				// this 'a' is an index for symbolTable
				
				for (int j = 0; j < numDef; j++) {
					
					// first, get the symbol of the definition
					String symbolName = input.next();
					
			
					// check if it was defined already before
					
					// I should invoke an error message here if it was defined here 
					 // set multiplyDefined to true
					
					
					/// enter into the for loop only when the symbol table has started to be constructing
					
					
			
					
					if (symbolTable.size() > 0) {
						for (int k = 0; k < symbolTable.size(); k++) {
							
							if (symbolTable.get(k).symbolName.equals(symbolName)) {  

								symbolTable.get(k).multiplyDefined = true;
								int relAddrs = input.nextInt();
								
								// the following line changes the absAddrs that was already computed
								// to the new absolute address
							
								/// has to be changing in the kth index because
								// we want to make change in our symbol table
								symbolTable.get(k).absAddrs = moduleBaseAddrs + relAddrs;

								
							}
							
							else {
								symbolTable.get(symbolTable.size()-1).multiplyDefined = false;
								int relAddrs = input.nextInt();
								
								// I don't know if I'll need to be storing this relative address
								// in the Definition object
								// since I'll be calculating absolute address 
								// and relative address is only for calculating absolute addresses
								symbolTable.add(new Definition(i, symbolName, relAddrs)); 
								symbolTable.get(symbolTable.size()-1).absAddrs = moduleBaseAddrs + relAddrs;	
		
								
								/// this return statement exists so that it doesn't go back to the loop
								// and say that we have multiply defined symbol
								break;
							}
						
						}
					}
					// if the symbol could not be found in the symbol table,
					// then it is not multiplyDefined,
					// so set its value to false
					
					// then just compute the absolute address given by the input
					// and create a new Definition object and add its information
					// to the symbol Table
					
					// when printing out the error message,
					// I could loop through the symbol table and if
					// symbolTable.get(k) (means if true) : print out that it's been multiply defined
					// and that the last value, symbolTable.get(k).absAddrs was used
					// and say that it was defined in symbolTable.get(k).moduleNum
					
					
					/// this is also wrong.....
					//// it could have already been constructed but have nothing conflicting
					
					//// in this case, it is when the symbol table size is 0
					/// which means that we can just work on the jth indix
					// this is i guess a bit inefficient because j'th index will only be used in this case
					/// of when the symbolTable size is 0
					else {
					
						int relAddrs = input.nextInt();
						
						// I don't know if I'll need to be storing this relative address
						// in the Definition object
						// since I'll be calculating absolute address 
						// and relative address is only for calculating absolute addresses
						symbolTable.add(new Definition(i, symbolName, relAddrs)); 
						symbolTable.get(j).absAddrs = moduleBaseAddrs + relAddrs;
						
						symbolTable.get(j).multiplyDefined = false;
					}

				}
				////////////////////////////////////////////////////////////////////////
			
				
				
				
				
				
				///////////////////USE LIST////////////////////////////////////////////
				int useListPair = input.nextInt();
				
				
				/// I think next() followed right after by nextInt() is wrong
				// when I want to go back from nextInt() to next(),
				/// I have to do input.next() in between
				// to not skip anything
				
				for (int m = 0; m < useListPair; m++) {
					
					// read in the name of the used symbol and its relative address
					// so that we could compute the absolute address of it
					// and store it in a hashMap
					
					// when we stick this external reference to the instruction memory map,
					// we'll be popping it
					//// the thing is, when we see the same symbol used, we want to merge those two usedLoc to that
					//// single symbol
					String usedSymbol = input.next();
				
					/// if it already exists
					int existingIndx = 0;
					boolean existing = false;
					for (int a = 0; a < usedList.size(); a++) {
						if (usedList.get(a).usedSymbolName.equals(usedSymbol))  {
							existingIndx = a;
							existing = true;
						}
						
					}
					
					// create an arraylist for the uselist hashmap
					ArrayList<Integer> usedLoc = new ArrayList<Integer>();
					
					
					// starting input, it can be any number
					int locationInput = -3;
					do {
						locationInput = input.nextInt();
						/// this -1 value can mess up the computation
						if (locationInput == -1) 
							break;
						
						int newAdd = locationInput + moduleBaseAddrs;
						
						////////////////////////////
						
						///// checking for multiply used in one instruction
						for (int h = 0; h < usedList.size(); h++) {
							ArrayList<Integer> tempArr = usedList.get(h).usedLocList;
							
							for (int x = 0; x < tempArr.size(); x++) {
								if (newAdd == tempArr.get(x)) { 
									Integer newAddIndx = newAdd;
									usedList.get(h).usedLocList.remove(newAddIndx);
									multiplyUsedError.add(newAddIndx);

					
								}
							}
						}
						//////////////////////////////////////////////////////////////////
						
						//// have to store the baseAddress (the first number we see in our column)
						// if we add it in this pattern, we don't need to compute its absolute address each time
						// and we have to link this arraylist to the corresponding usedSymbol
						usedLoc.add(newAdd);
					
						
					} while (locationInput != -1);

					
					
					// if that symbol already used elsewhere, just merge those two used locations
					if (existing) {
						usedList.get(existingIndx).usedLocList.addAll(usedLoc);
					}
					// if this symbol seen in the usedlist for the first time, just add it
					else {
						usedList.add(new Uses(i, usedSymbol, usedLoc));
						

					}

				}
				
				/////////////////////////////////////////////////////////////////////////
				
				
				
				//////////////INSTRUCTIONS//////////////////////////////////////////////
				// we'll be constructing a memoryMap in this instruction part
				int instrucNum = input.nextInt();
				
				// we'll be looping through by the number of instructions present in the module
				// we'll be storing each value in String and we'll be looking at the last digit each time
				// if the last digit is 1 or 2, we'll just take out the last 5th digit out, and store
				// the rest unchanged in the corresponding index of the memory map (just do .add() to the arrayList)
				// if the last digit is 3, we have to calculate for its absolute address
				// which can be computed by adding the input relative address + moduleBaseAddress
				// if the last digit is 4, we're going to look for that moduleBaseAddress from
				// the key inside of useListHash, and if they correspond, we're going to extract the
				// key and the value and store it in the corresponding index of the memoryMap as a string
				// and at the end, we're going to update the moduleBaseAddrs by the number of instructions there were
				// in that module
				
				for (int n = 0; n < instrucNum; n++) {
					String instruc = input.next();
					
					//// in the case of last digit 1 
					// in immediate case, I'm going to add the full 5 digit instruction
					// so that I can use that for error checking later
					if (instruc.charAt(4) == '1') {
						memoryMap.add(instruc);
					}
					
					// the absolute address represented by having X at the last digit
					else if (instruc.charAt(4) == '2') {
						memoryMap.add(instruc.substring(0, 4)+"X");
					}
					
					//// in the case of last digit 3 (i.e., relative address)
					else if (instruc.charAt(4) == '3') {
						int subRelative = Integer.parseInt(instruc.substring(0, 4));
						int newAbsAddrs = subRelative + moduleBaseAddrs;
						String strNewAbsAddrs = Integer.toString(newAbsAddrs);
						memoryMap.add(strNewAbsAddrs);
					}
					
					//// in the case of external references
					// look for that moduleBaseAddrs + n from the key of the usedList
					else if (instruc.charAt(4) == '4') {
						// in this case, n serves as a relative address
						// since it tells us where in the instruction list the current instruction is located
						int indx = moduleBaseAddrs + n;
						
						for (int c = 0; c < usedList.size(); c++) {
							if (usedList.get(c).usedLocList.contains(indx)) {
								String externalRefInfo = usedList.get(c).usedSymbolName + " " + instruc;
								memoryMap.add(externalRefInfo);
								
							}
						}
					}
					
					
				}
				
				moduleBaseAddrs += instrucNum;


			}
			break;
		
		}
		
		
		
		
		int symbolAbsAdrs = 0;
		String symbolAbsAdrsStr = "";
		
		for (int i = 0; i < memoryMap.size(); i++) {
			// check the first digit
			
			
			/// this means external referenced symbol used at index i
			if ((int)memoryMap.get(i).charAt(0) < 48 || (int)memoryMap.get(i).charAt(0) > 57) {
				String[] externalRef = memoryMap.get(i).split(" ");

				for (int j = 0; j < symbolTable.size(); j++) {
					
					/// then it'd be nice if I could find all the addresses that I need
					/// to replace with its absolute address which are stored in usedList 
					/// so what I would like to do now is to iterate through the usedList
					/// and store the indices temporarily as I go through, 
					/// then give them their new address concatenated with its absolute address
					/// and move on to the next indices
					/// and at the end move on to the next used symbol
	
					/// this if statement means that that used symbol is defined somewhere in the symbol table
					if (externalRef[0].equals(symbolTable.get(j).symbolName)) {
						
						////// definedButNotUsed false here!!!!!!!!!!!!
						symbolTable.get(j).definedButNotUsed = false; 
						if (symbolTable.get(j).absAddrs > (moduleBaseAddrs-1)) {
							symbolAbsAdrs = symbolTable.get(j).absAddrs-1;
						}
						
						else {
							symbolAbsAdrs = symbolTable.get(j).absAddrs;  ///////think of how i would use this variable
						}
						symbolAbsAdrsStr = Integer.toString(symbolAbsAdrs);
						String newAddrs = "";
						
						
						
						// if this used definition was found in the symbol table,
						// it is indeed defined and thus,
						// find the used location of that symbol
						// so that we can replace all those addresses with the corresponding symbol's absolute address
						for (int k = 0; k < usedList.size(); k++) {
							
							if (usedList.get(k).usedSymbolName.equals(externalRef[0])) {
								// an arraylist that contains all the used location of the 
								// current used symbol we're looking at
								ArrayList<Integer> intArr = usedList.get(k).usedLocList;
								for (int n = 0; n < intArr.size(); n++) {
									int needToBeChangedIndx = intArr.get(n);
									
									
									if (symbolAbsAdrsStr.length() == 1) {
										newAddrs = externalRef[1].substring(0, 1) + "00" + symbolAbsAdrsStr;
										memoryMap.set(i, newAddrs);


									}
									else if (symbolAbsAdrsStr.length() == 2) {
										newAddrs = externalRef[1].substring(0,1)+ "0" + symbolAbsAdrsStr;
										memoryMap.set(i, newAddrs);


									}
									
									else if (symbolAbsAdrsStr.length() == 3) {
										newAddrs = externalRef[1].substring(0, 1)+ symbolAbsAdrsStr;
										memoryMap.set(i, newAddrs);
									}
							}
						}
						}
					}
				
					}	
				
				for (int z = 0; z < memoryMap.size(); z++) {
					if ((int)memoryMap.get(i).charAt(0) < 48 || (int)memoryMap.get(i).charAt(0) > 57) {
						
						String errorMsg = externalRef[1].substring(0, 1) + "111 Error: " + externalRef[0] + " is not defined; 111 used.";
						
						///// setting the error message in the corresponding instruction space
						memoryMap.set(i, errorMsg);
					}
				}
	
						
					
				}
			}
			
		

		
		System.out.println("Symbol Table");
		for (int i = 0; i < symbolTable.size(); i++) {
			if (symbolTable.get(i).absAddrs > (moduleBaseAddrs-1)) System.out.println(symbolTable.get(i).symbolName+"="+ (symbolTable.get(i).absAddrs-1) + " Error: Definition exceeds module size; last word in module used.");
			
			else if (symbolTable.get(i).multiplyDefined) 
				System.out.println(symbolTable.get(i).symbolName+"="+symbolTable.get(i).absAddrs + " Error: This variable is multiply defined; last value used.");
			
			else 
				System.out.println(symbolTable.get(i).symbolName+"="+symbolTable.get(i).absAddrs);
		}
		
		System.out.println();
		
		System.out.println("Memory Map");
		
		String iStr = "";

		for (int i = 0; i < memoryMap.size(); i++) {
			
			
			if (Integer.toString(i).length() == 1) {
				iStr = Integer.toString(i) + ":  ";
			}
			else if (Integer.toString(i).length() == 2) {
				iStr = Integer.toString(i) + ": ";
				
			}
			
			else if (Integer.toString(i).length() == 3) {
				iStr = Integer.toString(i) + ":";
			}
			
			for (int j = 0; j < multiplyUsedError.size(); j++) {
				
				if (multiplyUsedError.get(j) == i) {
					System.out.printf("%s%s\n", iStr, memoryMap.get(i) + " Error: Multiple variables used in instruction; all but last ignored.");
					i++;
				}
				
			}
						
			
			// only when the absolute addresses' exceed machine size, print out this error
			if (memoryMap.get(i).length() == 5 && memoryMap.get(i).charAt(4) == 'X' && Integer.parseInt(memoryMap.get(i).substring(1, 4)) > 299) {
				String newAddrs = memoryMap.get(i).substring(0, 1) + "299";
				System.out.printf("%s%s\n", iStr, newAddrs + " Error: Absolute address exceeds machine size; largest legal value used.");
			}
			
			
			else if (memoryMap.get(i).length() == 5) {
				System.out.printf("%s%s\n", iStr, memoryMap.get(i).subSequence(0, 4));
			}
			
			else {
				System.out.printf("%s%s\n", iStr, memoryMap.get(i));

			}
		}

		
		System.out.println();
		for (int i = 0; i < symbolTable.size(); i++) {
			
			
			if (symbolTable.get(i).definedButNotUsed) {
				System.out.println("Warning: " + symbolTable.get(i).symbolName + " was defined in module " + symbolTable.get(i).moduleNum + " but never used.");
			}
		}
	
		
		
		
		

		}
		
		

	}

