package com.test.utilities;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Properties;


public class PropUtility {
private Properties propertyFile = null;
private FileInputStream fis = null;

public Properties loadFile(String filename) {
	propertyFile = new Properties();
	String propertyFilePath = null;
	
	switch (filename) {
	case "employeesproperties":
		propertyFilePath = ApiSourcePath.EMPLOYEES_prop_path;
		break;
	}
	try {
		fis = new FileInputStream(propertyFilePath);
		try {
			propertyFile.load(fis);
		} catch (FileNotFoundException e) {

			e.printStackTrace();
		}
	} catch (IOException e) {

		e.printStackTrace();
	}

	return propertyFile;
}

public String getPropertyValue(String Key) {
	String value = propertyFile.getProperty(Key);
	return value;
}
}
