package me.ploow.utils;

import java.util.ArrayList;

public class Settings {
	private ArrayList<String> shiftList, autoList = new ArrayList<>();

	public ArrayList<String> getShiftList() {
		return shiftList;
	}

	public void setShiftList(ArrayList<String> shiftList) {
		this.shiftList = shiftList;
	}

	public ArrayList<String> getAutoList() {
		return autoList;
	}

	public void setAutoList(ArrayList<String> autoList) {
		this.autoList = autoList;
	}
	
    public String getNameList(Boolean getAll, int index, ArrayList<String> list) {
        if (getAll) return list.toString();
        return list.get(index);
    }
    
    public Boolean hasNameList(String name, ArrayList<String> list) {
        if (list.contains(name)) return true;
        return false;
    }

    public void addInList(String name, ArrayList<String> list) {
        list.add(name);
    }

    public void removeFromList(String name, ArrayList<String> list) {
        list.remove(name);
    }

    public boolean isNumeric(String strNum) {
        try {
            Integer.parseInt(strNum);
        } catch (NumberFormatException nfe) {
            return false;
        }
        return true;
    }
    
    
    
}