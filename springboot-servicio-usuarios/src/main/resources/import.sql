INSERT INTO usuarios (username, password, enabled, nombre, apellido, email) values ('alejandro','$2a$10$Ij5j4FyB8tJ.iIs/Ly0SJ.TQhSFXI36SElgb/Cz0JHClCHmCAOTWK', true,'Alejandro','Larrosa','alejandro@gmail.com');
INSERT INTO usuarios (username, password, enabled, nombre, apellido, email) values ('juan','$2a$10$uPlmRN964PP4Qm7uq3ffGON7x.ny8EKol2XrtOc.A29Th4Ix2J72i',true,'Juan','Pinar','juan@gmail.com');

INSERT INTO roles (nombre) values ('ROLE_ADMIN');
INSERT INTO roles (nombre) values ('ROLE_USER');


INSERT INTO usuarios_to_roles (usuario_id, role_id) values (1,1);
INSERT INTO usuarios_to_roles (usuario_id, role_id) values (1,2);
INSERT INTO usuarios_to_roles (usuario_id, role_id) values (2,2);