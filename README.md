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

### 18 Notas:
- Desde la versión 2.4 de spring boot en adelante no es compatible con Ribbon.
- Eureka utiliza spring-cloud load balancer
- Por eso en este enfoque se cambia de versión de spring boot y de spring cloud.
- Zuul no es compatible con la versión 2.4 de spring boot y futuras.
- Ribbon utiliza round robbin

