package com.example.demo;

public class Util {

	/**
	 * strからopenedとclosedの数値を返却する
	 * @param str
	 * @return [0]: opened, [1]:closed
	 */
	public int[] returnOpenedAndClosedFromStr(String str) {
    	String subStr = "\"closed\":";
    	int index = str.indexOf(subStr);
    	int length = subStr.length();
    	int length2 = index + length;
        String strFoward = str.substring(length2);
        int index2 = strFoward.indexOf(",");
        String strFoward2 = strFoward.substring(0, index2);
        int retClose = Integer.parseInt(strFoward2);

    	subStr = "\"opened\":";
    	index = str.indexOf(subStr);
    	length = subStr.length();
    	length2 = index + length;
        strFoward = str.substring(length2);
        index2 = strFoward.indexOf("}");
        strFoward2 = strFoward.substring(0, index2);
        int retOpen = Integer.parseInt(strFoward2);

        int retArr[] = new int[2];
        retArr[0] = retOpen;
        retArr[1] = retClose;

        return retArr;
	}

	/**
	 * strからopenedの数値を返却する
	 * @param str
	 * @return openedの数値
	 */
	public int returnOpenedFromStr(String str) {
    	String subStr = "\"opened\":";
    	int index = str.indexOf(subStr);
    	int length = subStr.length();
    	int length2 = index + length;
        String strFoward = str.substring(length2);
        int index2 = strFoward.indexOf("}");
        String strFoward2 = strFoward.substring(0, index2);
        return Integer.parseInt(strFoward2);

	}
}
