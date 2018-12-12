package com.monitor.humiture.vo;

import lombok.Data;

@Data
public class ResultVO<T> {

    private Integer code;

    private String msg;

    private T data;


    public static class ResultBuilder{

        public static ResultVO getOK(Object object){
            ResultVO result = new ResultVO();
            result.setCode(200);
            result.setMsg("成功");
            result.setData(object);
            return result;
        }


        public static ResultVO getOK(){
            return getOK(null);
        }

        public static ResultVO getError(Integer code,String msg){
            ResultVO result = new ResultVO();
            result.setCode(code);
            result.setMsg(msg);
            return result;
        }

        public static ResultVO getError(String msg){
            ResultVO result = new ResultVO();
            result.setMsg(msg);
            result.setCode(405);
            return result;
        }


    }

}
