

package com.github.peonyking.http.error;

/**
 * Created by ThirtyDegreesRay on 2017/10/12 14:35:50
 */

public class UnauthorizedError extends HttpError {

    public UnauthorizedError() {
        super(HttpErrorCode.UNAUTHORIZED);
    }

}
