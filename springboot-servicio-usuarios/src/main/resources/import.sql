INSERT INTO usuarios (username, password, enabled, nombre, apellido, email) values ('alejandro','12345',1,'Alejandro','Larrosa','alejandro@gmail.com');
INSERT INTO usuarios (username, password, enabled, nombre, apellido, email) values ('juan','54321',1,'Juan','Pinar','juan@gmail.com');

INSERT INTO roles (nombre) values ('ROLE_ADMIN');
INSERT INTO roles (nombre) values ('ROLE_USER');


INSERT INTO usuarios_to_roles (usuario_id, role_id) values (1,1);
INSERT INTO usuarios_to_roles (usuario_id, role_id) values (2,2);
INSERT INTO usuarios_to_roles (usuario_id, role_id) values (2,1);