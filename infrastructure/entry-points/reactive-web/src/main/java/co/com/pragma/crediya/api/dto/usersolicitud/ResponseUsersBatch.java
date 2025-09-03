package co.com.pragma.crediya.api.dto.usersolicitud;

import java.util.List;

public record ResponseUsersBatch (
        List<UserLiteDTO> results
){
}
