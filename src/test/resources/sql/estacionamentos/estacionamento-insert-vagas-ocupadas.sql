insert into usuarios (id, username , password ,role) values (100, 'ana@email.com','$2a$12$plQrc7DiZCHWgI5G3rXchuKAxC9QG/w1PjJFFjKWXT.Zi1K9prKda', 'ROLE_ADMIN');
insert into usuarios (id, username , password ,role) values (101, 'bia@email.com','$2a$12$plQrc7DiZCHWgI5G3rXchuKAxC9QG/w1PjJFFjKWXT.Zi1K9prKda', 'ROLE_CLIENT');
insert into usuarios (id, username , password ,role) values (102, 'bob@email.com','$2a$12$plQrc7DiZCHWgI5G3rXchuKAxC9QG/w1PjJFFjKWXT.Zi1K9prKda', 'ROLE_CLIENT');
insert into usuarios (id, username , password ,role) values (103, 'toby@email.com','$2a$12$plQrc7DiZCHWgI5G3rXchuKAxC9QG/w1PjJFFjKWXT.Zi1K9prKda', 'ROLE_CLIENT');


insert into clientes (id, nome,cpf,id_usuario) values (10,'Bianca Silva', '79074426050', 101);
insert into clientes (id, nome,cpf,id_usuario) values (20,'Roberto Gomes', '55352517047', 102);

insert into vagas (id, codigo, status) values (100, 'A-01', 'OCUPADA');
insert into vagas (id, codigo, status) values (200, 'A-02', 'OCUPADA');
insert into vagas (id, codigo, status) values (300, 'A-03', 'OCUPADA');
insert into vagas (id, codigo, status) values (400, 'A-04', 'OCUPADA');
insert into vagas (id, codigo, status) values (500, 'A-05', 'OCUPADA');

insert into clientes_tem_vagas ( id, numero_recibo, placa, marca, modelo, cor, data_entrada, id_cliente, id_vaga)
    values ( 100,'20230313-101300', 'FIT-1020', 'FIAT', 'PALIO', 'VERDE', '2024-08-16 17:15:00', 20, 100);
insert into clientes_tem_vagas (id, numero_recibo, placa, marca, modelo, cor, data_entrada, id_cliente, id_vaga)
    values ( 101, '20230314-101400', 'SIE-1020', 'FIAT', 'SIENA', 'BRANCO', '2024-08-16 17:15:00', 10, 200);
insert into clientes_tem_vagas (id, numero_recibo, placa, marca, modelo, cor, data_entrada, id_cliente, id_vaga)
    values ( 102, '20230315-101500', 'FIT-1020', 'FIAT', 'PALIO', 'VERDE', '2024-08-16 17:15:00', 20, 300);

