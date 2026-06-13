import { HttpInterceptorFn } from '@angular/common/http';

export const authInterceptor: HttpInterceptorFn = (req, next) => {
  // Grab the token from Local Storage
  const token = localStorage.getItem('jwt_token');

  // If we have a token, clone the request and attach the Authorization header
  if (token) {
    const clonedRequest = req.clone({
      setHeaders: {
        Authorization: `Bearer ${token}`
      }
    });
    return next(clonedRequest);
  }

  // If no token (like when logging in), just send the request normally
  return next(req);
};