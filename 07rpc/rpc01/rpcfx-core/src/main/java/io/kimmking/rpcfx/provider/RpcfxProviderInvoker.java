package io.kimmking.rpcfx.provider;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.serializer.SerializerFeature;
import io.kimmking.rpcfx.api.RpcContext;
import io.kimmking.rpcfx.api.RpcfxRequest;
import io.kimmking.rpcfx.api.RpcfxResponse;
import io.kimmking.rpcfx.meta.ProviderMeta;
import org.springframework.util.CollectionUtils;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.List;
import java.util.Optional;

public class RpcfxProviderInvoker {

    RpcContext context;

    public RpcfxProviderInvoker(RpcContext context) {
        this.context = context;
    }

    public RpcfxResponse invoke(RpcfxRequest request) {
        RpcfxResponse response = new RpcfxResponse();
        String serviceClass = request.getServiceClass();

        ProviderMeta meta = findProvider(serviceClass, request.getMethodSign());

        try {
            Method method = meta.getMethod();
            // 没有控制超时，所以可能会很久 TODO 1
            Object result = method.invoke(meta.getServiceImpl(), request.getParams()); // dubbo, fastjson,
            // 两次json序列化能否合并成一个
            response.setResult(JSON.toJSONString(result, SerializerFeature.WriteClassName));
            response.setStatus(true);
            return response;
        } catch ( IllegalAccessException | InvocationTargetException e) {

            // 3.Xstream

            // 2.封装一个统一的RpcfxException
            // 客户端也需要判断异常
            e.printStackTrace();
            response.setException(e);
            response.setStatus(false);
            return response;
        }
    }

    protected ProviderMeta findProvider(String interfaceName, String methodSign) {
        List<ProviderMeta> providerMetas = context.getProviderHolder().get(interfaceName);
        if (!CollectionUtils.isEmpty(providerMetas)) {
            Optional<ProviderMeta> providerMeta = providerMetas.stream()
                    .filter(provider -> methodSign.equals(provider.getMethodSign())).findFirst();
            return providerMeta.orElse(null);
        }
        return null;
    }

}
