# microservicios_con_spring_boot_y_spring-cloud_netflix_eureka

## Lecciones curso
- Se crea microservicio productos
- Se crea microservicio items.
- Se consulta microservicio productos desde microservicio items con REST TEMPLATE
- Se consulta microservicio productos desde microservicio items con OPEN FEIGN.
- Balanceo de carga con Ribbon con OPEN FEIGN.
- Balanceo de carga con Riboon con RestTemplate.
- Creando servidor eureka y conectando los dos microservicios anteriores al mismo.
- Puerto dinámico para el microservicio de productos.
- Hystrix: tolerancia a fallos: permite enviar el flujo de código a otro sitio cuando se produce un error en el micro de productos.
  Por defecto, cuando un micro llama a otro, si ese otro tarda en responder más de x tiempo, se produce un error de timeout. Yo lo he probado y no me ha funcionado.
  Lo que se hace para solventar eso es añadir las properties para ampliar ese timeout de llamada de un microservicio a otro microservicio.
    hystrix.command.default.execution.isolation.thread.timeoutInMilliseconds: 20000
    ribbon.ConnectTimeout: 3000
    ribbon.ReadTimeout: 10000
- Zuul server: es un servidor para gestionar el api-gateway. 
    - Se implementan filtros pre y post para controlar los eventos de antes y después de lanzar la petición http.
- Spring cloud API Gateway: sustituye a Zuul server para versiones más modernas de springboot. En este caso se utiliza la versión 2.5.3 de springboot, la cual es compatible con la versión 2020.0.3 de spring cloud

### 18 Notas:
- Desde la versión 2.4 de spring boot en adelante no es compatible con Ribbon.
- Eureka utiliza spring-cloud load balancer
- Por eso en este enfoque se cambia de versión de spring boot y de spring cloud.
- Zuul no es compatible con la versión 2.4 de spring boot y futuras.
- Ribbon utiliza round robbin
- Hystrix sólo es compatible con versiones anteriores a 2.4. A partir de ahí ya se usa Resilience4j
