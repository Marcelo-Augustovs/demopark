insert into usuarios (id, username , password ,role) values (100, 'ana@email.com','$2a$12$plQrc7DiZCHWgI5G3rXchuKAxC9QG/w1PjJFFjKWXT.Zi1K9prKda', 'ROLE_ADMIN');
insert into usuarios (id, username , password ,role) values (101, 'bia@email.com','$2a$12$plQrc7DiZCHWgI5G3rXchuKAxC9QG/w1PjJFFjKWXT.Zi1K9prKda', 'ROLE_CLIENT');
insert into usuarios (id, username , password ,role) values (102, 'bob@email.com','$2a$12$plQrc7DiZCHWgI5G3rXchuKAxC9QG/w1PjJFFjKWXT.Zi1K9prKda', 'ROLE_CLIENT');
insert into usuarios (id, username , password ,role) values (103, 'toby@email.com','$2a$12$plQrc7DiZCHWgI5G3rXchuKAxC9QG/w1PjJFFjKWXT.Zi1K9prKda', 'ROLE_CLIENT');


insert into clientes (id, nome,cpf,id_usuario) values (10,'Bianca Silva', '79074426050', 101);
insert into clientes (id, nome,cpf,id_usuario) values (20,'Roberto Gomes', '55352517047', 102);