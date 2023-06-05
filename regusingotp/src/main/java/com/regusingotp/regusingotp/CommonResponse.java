package com.regusingotp.regusingotp;




        import lombok.Data;
        import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class CommonResponse {
    String message;
    Boolean status;




    public String getMessage(String otpVerifiedYouCanLogin) {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public Boolean getStatus() {
        return status;
    }

    public void setStatus(Boolean status) {
        this.status = status;
    }
}
