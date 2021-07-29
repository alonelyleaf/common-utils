# prometheus-target-discovery

目前 prometheus 仅支持从consul 获取服务，由于当前服务使用 nacos 作为注册中心，需要增加 adapter 服务，实现prometheus使用consul_sd_config配置时需要的http接口，具体实现的接口如下：
- /v1/agent/self 返回默认的datacenter
- /v1/catalog/services 返回nacos中的服务列表
- /v1/catalog/service/{service} 返回服务实例
- /v1/health/service/{service} 返回服务健康信息

本服务实现上述的接口，实现从nacos中获取服务并返回给prometheus。在prometheus.yml中发现配置如下：

```yaml
scrape_configs:
  # 基于服务发现，consul 的target配置，https://www.prometheus.wang/sd/service-discovery-with-consul.html
  - job_name: 'nacos-prometheus'
    # 采集exporter时的接口路径
    metrics_path: '/actuator/prometheus'
    consul_sd_configs:
      # 此服务部署的地址
    - server: 'localhost:8080'
      services: [] 
    # Relabeling基于Target实例中包含的metadata标签，动态的添加或者覆盖标签。此处添加应用信息及环境。
    relabel_configs:
    - source_labels:  ["__meta_consul_service"]
      target_label: "application"
    - source_labels:  ["__meta_consul_service_metadata_profile"]
      target_label: "env" 
```
