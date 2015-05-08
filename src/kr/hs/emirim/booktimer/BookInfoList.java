package kr.hs.emirim.booktimer;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.ArrayList;

import android.content.Context;

public class BookInfoList {

	public ArrayList<BookInfo> theList;
	
	public Context theContext;
		
	public BookInfoList(Context aContext){
		theContext = aContext;
		theList = new ArrayList<BookInfo>();
	}
	
	public int AddContact(String aTitle, String aWirter, String aPublish){
		
		BookInfo tmpB = new BookInfo();
		tmpB.bTitle = aTitle;
		tmpB.bWriter = aWirter;
		tmpB.bPublish = aPublish;
		tmpB.bReadChk = false;
		
		theList.add(tmpB);
		
		return 1;
	}
	
	public int LoadData(String aFileName){
		FileInputStream tmpFIS = null;
		ObjectInputStream tmpOIS;
		try {
			tmpFIS = theContext.openFileInput(aFileName);
		} catch (Exception e) {
			tmpFIS = null;
		}
		
		if(tmpFIS == null){
			return 0;
		}
		try {
			tmpOIS = new ObjectInputStream(tmpFIS);
			Object tmpObj = tmpOIS.readObject();
			if(tmpObj instanceof ArrayList<?>){
				theList = (ArrayList<BookInfo>) tmpObj;
			}
		} catch (IOException e) {
			return 0;
		}
		catch(ClassNotFoundException e){
			return 0;
		}
		return 1;
	}
	
	public int SaveData(String aFileName){
		FileOutputStream tmpFOS = null;
		ObjectOutputStream tmpOOS = null;
		
		if(theContext == null){
			return 0;
		}
		try {
			tmpFOS = theContext.openFileOutput(aFileName, Context.MODE_PRIVATE);
		} catch (Exception e) {
			tmpFOS = null;
		}
		
		if(tmpFOS == null){
			return 0;
		}
		
		try {
			tmpOOS = new ObjectOutputStream(tmpFOS);
			tmpOOS.writeObject(theList);
		} catch (IOException e) {
			return 0;		
		}
		return 1;
	}
	
	public int DeleteDate(String aFileName){
		if(theContext == null){
			return 0;
		}
		theContext.deleteFile(aFileName);		
		return 1;
	}
}
