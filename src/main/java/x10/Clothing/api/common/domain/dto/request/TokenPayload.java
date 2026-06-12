package x10.Clothing.api.common.domain.dto.request;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

@Builder
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class TokenPayload {
    private String userId;
    private List<String> roleIds;
    private List<String> roles;
    private String username;
    private String email;

}
