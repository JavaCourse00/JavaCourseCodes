package io.kimmking.rpcfx.api;

import lombok.Data;

@Data
public class RpcfxRequest {
  private String serviceClass;
  private String methodSign;
  private Object[] params;
}
