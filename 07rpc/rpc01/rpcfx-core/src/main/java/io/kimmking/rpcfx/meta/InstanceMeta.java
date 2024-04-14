package io.kimmking.rpcfx.meta;

import com.google.common.base.Strings;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

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
public class InstanceMeta {

    private String scheme;
    private String ip;
    private Integer port;
    private String context;
    private String status;
    private Map<String, String> metadata;

    public static InstanceMeta from(String instance) {
        URI uri = URI.create(instance);
        String path = uri.getPath();
        path = Strings.isNullOrEmpty(path) ? "" : path.substring(1);
        return InstanceMeta.builder()
                .scheme(uri.getScheme())
                .ip(uri.getHost())
                .port(uri.getPort())
                .context(path)
                .build();
    }

    @Override
    public String toString() {
        return scheme + "://" + ip + ":" + port + "/" + context;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        InstanceMeta that = (InstanceMeta) o;
        return Objects.equals(scheme, that.scheme) && Objects.equals(ip, that.ip) && Objects.equals(port, that.port) && Objects.equals(context, that.context);
    }

    @Override
    public int hashCode() {
        return Objects.hash(scheme, ip, port, context);
    }
}
