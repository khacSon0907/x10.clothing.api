package x10.Clothing.api.config.jwt;



public interface IJwtService {

    /**
     * Tạo Access Token từ TokenPayload
     */
    String generateAccessToken(TokenPayload payload);

    /**
     * Tạo Refresh Token từ TokenPayload
     */
    String generateRefreshToken(TokenPayload payload);

    /**
     * Validate Access Token
     */
    TokenPayload validateAccessToken(String token);

    /**
     * Validate Refresh Token
     */
    TokenPayload validateRefreshToken(String token);


    /**
     * Kiểm tra token có hết hạn không
     */
    boolean isTokenExpired(String token);


    String getUserIdFromToken(String token);
    long getRemainingExpiration(String token);
}
