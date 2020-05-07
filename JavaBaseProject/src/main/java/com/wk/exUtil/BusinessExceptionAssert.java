package com.wk.exUtil;


public interface BusinessExceptionAssert extends IResponseEnum, Assert {
    @Override
    default BaseException newException(Object... args) {
        // String msg = MessageFormat.format(this.getMessage(), args);
        String msg = this.getMessage();
        return new BusinessException(this, args, msg);
    }

    @Override
    default BaseException newException(Throwable t, Object... args) {
        // String msg = MessageFormat.format(this.getMessage(), args);
        String msg = this.getMessage();
        return new BusinessException(this, args, msg, t);
    }
}
