package com.monitor.humiture.constant;


public enum HumStatusEnum {

    ExtremelyHot("酷热","image/scorchingHot.jpg"),  //>36
    ScorchingHot("炎热","image/scorchingHot.jpg"),  //31-35
    Hotness("暑热","image/hotness.jpg"),       //25-30
    COOL("凉爽","image/cool.jpg"),          //18-24
    COLD_AND_CLEAR("清寒","image/cold.jpg"), //1-17
    COLD("寒冷","image/cold.jpg"),          //-10 - 0
    SevereCold("严寒","image/severeCold.jpg"),    //-20 - -11
    ABYSM("深寒","image/severeCold.jpg");         //<-20

    private String status;

    private String imgUrl;

    public String getStatus() {
        return status;
    }

    public String getImgUrl() {
        return imgUrl;
    }

    HumStatusEnum(String status, String imgUrl) {
        this.status = status;
        this.imgUrl = imgUrl;
    }

    HumStatusEnum() {
    }
}
