package cn.edu.bucm.exceptions;

public class ThePicHasNotFaceException extends Exception {
    /*无参构造函数*/
    public ThePicHasNotFaceException(){
        super();
    }

    //用详细信息指定一个异常
    public ThePicHasNotFaceException(String message){
        super(message);
    }
}
