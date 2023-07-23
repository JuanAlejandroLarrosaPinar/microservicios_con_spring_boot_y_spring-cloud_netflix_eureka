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
  - Por defecto, cuando un micro llama a otro, si ese otro tarda en responder más de x tiempo, se produce un error de timeout. Yo lo he probado y no me ha funcionado. Lo que se hace para solventar eso es añadir las properties para ampliar ese timeout de llamada de un microservicio a otro microservicio.
    - hystrix.command.default.execution.isolation.thread.timeoutInMilliseconds: 20000
    - ribbon.ConnectTimeout: 3000
    - ribbon.ReadTimeout: 10000
- Zuul server: es un servidor para gestionar el api-gateway. 
    - Se implementan filtros pre y post para controlar los eventos de antes y después de lanzar la petición http.
- Spring cloud API Gateway: sustituye a Zuul server para versiones más modernas de springboot. En este caso se utiliza la versión 2.5.3 de springboot, la cual es compatible con la versión 2020.0.3 de spring cloud
  - Filtros pre y post con GlobalFilter y con AbstractGatewayFilterFactory (modificando en ambos casos la request y la response añadiendo headers y cookies).
  - Filtros personalizados de spring que no hace falta programarlos
  - Predicados: condiciones que tienen que cumplir las peticiones para que el cliente las encuentre como tal.
- Resilience: sustituye a hystrix. Si se produce una excepción en un servicio llamado, el error se va propagando a los servicios que lo llaman. Hay un umbral de fallos. 
  - Hay 3 estados:
    - Cerrado: inicial. Si hay x errores, entonces se abre el circuitbreaker.
    - Abierto: se han producido muchos errores y se abre el cortocircuito
    - Semiabierto: se realizan peticiones de un micro a otro para saber si ya está ok.
  - Configuraciones:
    - slidingWindowSize(100): por defecto son 100. De cada cien...se establece un porcentaje falla. Si el porcentaje es mayor que los criterios, se abre el  cortocircuito.
    - failureRateThreshold (50): de cada 100 peticiones, si 50 fallan, se abre el cortocircuito.
    - waitDurationInOpenState (60000 ms): tiempo que permanece el cortocircuito abierto.
    - permittedNumberOfCallsInHalfOpenState(10): hará 10 peticiones de prueba en el caso de que el estado del cortocircuito sea semiabierto.
    - slowCallRateThreshold (100): si de 100 peticiones, las 100 son lentas, se abre el cortocircuito
    - slowCallDurationThreshold (60000 ms): esta propiedad es paara considerar que una llamada es lenta.
  - Con la configuración por defecto lo que hace es:
    - Si hacemos 55 peticiones con error y 45 bien, al completar el ciclo de 100, al ejecutar la 101 pasa por el metodo alternativo (como si estuviese fallando). Esto indica que se ha abierto el cortocircuito 
    - Tras un minuto ya no se trata de 100 peticiones, sino de 10. Si de esas 10 fallan 6, se vuelve a abrir.
    - Y así constantemente.
- Circuit breaker va por un lado y TimeLimitter va por otro (aunque ambos se pueden combinar):
  - Circuit breaker se encarga de gestionar el hecho de que si hay más llamadas erroneas de lo normal, abra el cortocircuito y no permita más llamadas hasta pasado x tiempo
  - Timelimitter: sólo gestiona TimeOut.
-Se puede implementar CircuitBreaker y Timelimitter en el servicio-gateway, para no ir haciéndolo servicio por servicio. Aunque a mi sólo me ha funcionado la parte de timelimitter y no la de circuitbreacker (el fallbackUri no lo hace correctamente).  
  

    
  
### 18 Notas:
- Desde la versión 2.4 de spring boot en adelante no es compatible con Ribbon.
- Eureka utiliza spring-cloud load balancer
- Por eso en este enfoque se cambia de versión de spring boot y de spring cloud.
- Zuul no es compatible con la versión 2.4 de spring boot y futuras.
- Ribbon utiliza round robbin
- Hystrix sólo es compatible con versiones anteriores a 2.4. A partir de ahí ya se usa Resilience4j
