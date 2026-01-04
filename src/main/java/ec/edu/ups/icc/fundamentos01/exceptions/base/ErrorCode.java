package ec.edu.ups.icc.fundamentos01.exceptions.base;

public class ErrorCode extends RuntimeException {

    private String code;

    public ErrorCode(String code) {
        this.code = code;
    }
    
    public String getCode() {
        return code;
    }
    
}
