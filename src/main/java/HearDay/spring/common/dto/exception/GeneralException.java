package HearDay.spring.common.dto.exception;

import HearDay.spring.common.dto.code.BaseErrorCode;
import HearDay.spring.common.dto.code.ErrorReasonDTO;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class GeneralException extends RuntimeException {

    private BaseErrorCode code;

    public ErrorReasonDTO getErrorReason() {
        return this.code.getReason();
    }

    public ErrorReasonDTO getErrorReasonHttpStatus(){
        return this.code.getReasonHttpStatus();
    }

    public String getErrorCode() {
        return this.code.getReason().getCode();
    }
}