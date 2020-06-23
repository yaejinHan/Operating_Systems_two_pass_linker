import java.util.*;

public class Definition {
	
	
	String symbolName;
	int moduleNum;
	int relAddrs;
	int baseAddrs;
	int absAddrs;
	// its initial value is false
	boolean multiplyDefined;
	boolean definedButNotUsed = true; // I'll be changing this input when I'm on my second pass
	boolean definitionExceedsModuleSize;
	
	public Definition(int moduleNum, String symbolName, int relAddrs, int baseAddrs, int absAddrs) {
		this.moduleNum = moduleNum;
		this.symbolName = symbolName;
		this.relAddrs = relAddrs;
		this.baseAddrs = baseAddrs;
		this.absAddrs = absAddrs;
	}
	
	public Definition(int moduleNum, String symbolName, int relAddrs) {
		this.moduleNum = moduleNum;
		this.symbolName = symbolName;
		this.relAddrs = relAddrs;
	}
	
	
	
	public int getAbsAddrs(int relAddrs, int baseAddrs) {
		int absAddrs = baseAddrs + relAddrs;
		
		return absAddrs;
	}
	
	
	
	
	
	

}
