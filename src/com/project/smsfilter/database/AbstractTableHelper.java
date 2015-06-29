package com.project.smsfilter.database;

import java.util.ArrayList;

public abstract class AbstractTableHelper {

	abstract public ArrayList<String> getColumns();	
	abstract public String getTableName();
	abstract public String createSql();
	abstract public String updateSql();
	
}
