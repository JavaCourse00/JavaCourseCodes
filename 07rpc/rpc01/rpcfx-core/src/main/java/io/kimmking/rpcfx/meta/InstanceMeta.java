package io.kimmking.rpcfx.meta;

import com.google.common.base.Strings;
import lombok.*;

import java.net.URI;
import java.util.Map;
import java.util.Objects;

/**
 * Description for this class.
 *
 * @Author : kimmking(kimmking@apache.org)
 * @create 2024/2/8 19:46
 */

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(of = {"scheme", "host", "port", "context"})
public class InstanceMeta {

    private String scheme;
    private String host;
    private Integer port;
    private String context;
    private boolean status;
    private Map<String, String> metadata;

    public static InstanceMeta from(String instance) {
        URI uri = URI.create(instance);
        String path = uri.getPath();
        path = Strings.isNullOrEmpty(path) ? "" : path.substring(1);
        return InstanceMeta.builder()
                .scheme(uri.getScheme())
                .host(uri.getHost())
                .port(uri.getPort())
                .context(path)
                .build();
    }

    @Override
    public String toString() {
        return scheme + "://" + host + ":" + port + "/" + context;
    }

}
