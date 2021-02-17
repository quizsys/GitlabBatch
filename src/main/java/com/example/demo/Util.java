package com.example.demo;

import java.io.UnsupportedEncodingException;
import java.time.LocalDate;
import java.util.ArrayList;

import com.example.demo.burnDown.BurnDownDto;
import com.example.demo.dto.IssueModel;
import com.example.demo.dto.Milestone;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;

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


	/**
	 * マイルストーンのjsonから期間内のマイルストーン名を返す
	 * @param json
	 * @return
	 * @throws JsonMappingException
	 * @throws JsonProcessingException
	 */
	public String jsonToMilestoneDate(String json) throws JsonMappingException, JsonProcessingException {

//        LocalDate today = LocalDate.of(2021,1,12);
        LocalDate today = LocalDate.now();
        LocalDate yesteray = today.minusDays(1);

		ObjectMapper mapper = new ObjectMapper();
		Milestone[] modelList = mapper.readValue(json, Milestone[].class);

		String milestoneStr = "";

        //マイルストーン毎にループ回す
        for(Milestone model : modelList) {

        	//開始・終了日が設定されていない場合、スキップ
        	if(model.getStartDate() == null || model.getDueDate() == null ){
        		continue;
        	}

        	LocalDate startDate = LocalDate.parse(model.getStartDate());
        	LocalDate dueDate = LocalDate.parse(model.getDueDate());

//        	System.out.println(yesteray.toString());
//        	System.out.println(model.getTitle() + "," + model.getStartDate() + ", " + model.getDueDate() );

        	//期間内の場合、返却値に設定してループを抜ける
            if (yesteray.compareTo(startDate) >= 0 && yesteray.compareTo(dueDate) <= 0) {

            	milestoneStr = model.getTitle();
            	break;
            }
    	}

        return milestoneStr;
	}


	/**
	 * issuenのjson集計し、BurnDownのDtoに変換して返却する
	 * @param jsonList
	 * @param milestone
	 * @return
	 * @throws JsonMappingException
	 * @throws JsonProcessingException
	 */
	public ArrayList<BurnDownDto> jsonToDto(ArrayList<String> jsonList, String milestone) throws JsonMappingException, JsonProcessingException {

        LocalDate today = LocalDate.now();
        LocalDate yesteray = today.minusDays(1);
        String allLabel = "すべて";

        ArrayList<BurnDownDto> list = new ArrayList<>();
        BurnDownDto allDto = new BurnDownDto(milestone, yesteray, allLabel);
        list.add(allDto);

        for(String json : jsonList) {

//	        System.out.println(json.length());

			ObjectMapper mapper = new ObjectMapper();
        	IssueModel[] modelList = mapper.readValue(json, IssueModel[].class);

	        //ISSUE毎にループ回す
	        for(IssueModel model : modelList) {

	            //時間を取得
	        	int totalTimeSpent = model.getTimeStats().getTotalTimeSpent();
	        	int timeEstimate = model.getTimeStats().getTimeEstimate();
	        	int uncompTimeSpent = 0;

	        	//合計値に加算
	        	list.get(0).setTimeEstimate(list.get(0).getTimeEstimate() + timeEstimate);
	        	list.get(0).setTotalTimeSpent(list.get(0).getTotalTimeSpent() + totalTimeSpent);
	        	list.get(0).setAllIssueCount(list.get(0).getAllIssueCount() +1);

	        	//完了済みの時
	            if(model.getState().equals("closed") || model.getLabels().contains("Done")) {
	            	list.get(0).setCompIssueCount(list.get(0).getCompIssueCount() +1);
	            	list.get(0).setCompTimeEstimate(list.get(0).getCompTimeEstimate() + timeEstimate); //追加

            	//未完了の場合
	            } else {

	            	// 実績が見積時間の9割を超過する場合は、9割に計算
	            	if(totalTimeSpent >  (int)Math.round(timeEstimate * 0.9)) {
	            		uncompTimeSpent = (int)Math.round(timeEstimate * 0.9);
	            	} else {
		            	uncompTimeSpent = totalTimeSpent;
	            	}
	            	//合計値に加算
	            	list.get(0).setUncompTimeSpent(list.get(0).getUncompTimeSpent() + uncompTimeSpent); //追加

	            }

	            //ラベルごとに回す
	            for(String label : model.getLabels()) {

	            	int index = labelExsistCheck(list, label);

	            	if(index != -1) {
	                	//もしすでにラベルがある場合、追加
	            		list.get(index).setTimeEstimate(list.get(index).getTimeEstimate() + timeEstimate);
	            		list.get(index).setTotalTimeSpent(list.get(index).getTotalTimeSpent() + totalTimeSpent);
	            		list.get(index).setAllIssueCount(list.get(index).getAllIssueCount() +1);

	                    //完了済みの時
	    	            if(model.getState().equals("closed") || model.getLabels().contains("Done")) {
	                    	list.get(index).setCompIssueCount(list.get(index).getCompIssueCount() +1);
	    	            	list.get(index).setCompTimeEstimate(list.get(index).getCompTimeEstimate() + timeEstimate); //追加

    	            	//未完了の場合
	    	            } else {
	    	            	list.get(index).setUncompTimeSpent(list.get(index).getUncompTimeSpent() + uncompTimeSpent);
	                    }

	            	} else {

	            		//もしラベルがない場合、新規作成
	                	BurnDownDto dto = new BurnDownDto(milestone, yesteray, label);
	                    dto.setTimeEstimate(timeEstimate);
	                    dto.setTotalTimeSpent(totalTimeSpent);
	                    dto.setAllIssueCount(1);

	                    //完了済みの時
	    	            if(model.getState().equals("closed") || model.getLabels().contains("Done")) {
	                    	dto.setCompIssueCount(1);
	                    	dto.setCompTimeEstimate(timeEstimate); //追加
    	            	//未完了の場合
	    	            } else {
	                    	dto.setUncompTimeSpent(uncompTimeSpent);
	                    }
	                    list.add(dto);
	            	}
	            }
	        }

        }

//        System.out.println(list);

        return list;



	}

	private int labelExsistCheck(ArrayList<BurnDownDto> list, String label) {
		// TODO 自動生成されたメソッド・スタブ

		int ret = -1;
		for(int i=0; i< list.size(); i++) {
			if(label.equals(list.get(i).getLabel())) {
				ret = i;
				break;
			}
		}
		return ret;
	}

	public int jsonToIssueIid(String json) throws JsonMappingException, JsonProcessingException, UnsupportedEncodingException {
		// TODO 自動生成されたメソッド・スタブ

		ObjectMapper mapper = new ObjectMapper();
		IssueModel model = mapper.readValue(json, IssueModel.class);

		return model.getIid();

	}

}
