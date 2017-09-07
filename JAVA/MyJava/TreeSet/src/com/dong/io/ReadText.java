package com.dong.io;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class ReadText {
	/*
	 * ��ȡ�ļ�������Ϊ��λ��ȡ�ļ����ݣ�һ�ζ�һ����
	 */
	public List read(String fileName){
		File file = new File(fileName);
		BufferedReader br = null;
		List<String> arrayList = new ArrayList<String>();
		String str;
		try {
			br = new BufferedReader(new FileReader(fileName));
			// һ�ζ���һ�У�ֱ������nullΪ�ļ�����
			while((str = br.readLine())!=null){
				arrayList.add(str);
			}
			br.close();
		} catch (Exception e) {
			e.printStackTrace();
		}finally{
			if(br == null){
				try {
					br.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
		return arrayList;
	}
	
	String[] split(String str){
		String[] split = str.split(",");
		return split;
	}
}