import java.util.*;

public class Uses {
	
	boolean multiplyUsed = false;
	
	
	int moduleIndx;
	String usedSymbolName;
	ArrayList<Integer> usedLocList = new ArrayList<Integer>();
	
	public Uses(int moduleIndx, String usedSymbol, ArrayList usedLoc) {
		this.moduleIndx = moduleIndx;
		this.usedSymbolName = usedSymbol;
		this.usedLocList = usedLoc;
	}
	
	

}
