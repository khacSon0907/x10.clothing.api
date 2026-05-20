package x10.Clothing.api.service.authService.logoutUc;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

public interface ILogoutUc {
    void logout(HttpServletRequest request, HttpServletResponse response);
}
