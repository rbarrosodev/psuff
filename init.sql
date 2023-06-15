USE psuff;

CREATE TABLE aeroporto (
    id integer auto_increment primary key,
    codigo varchar(5) unique not null,
    endereco varchar(100) not null,
    nome varchar(100) unique not null,
    qtd_pistas integer,
    qtd_companhias integer,
    version integer
);

CREATE TABLE terminal (
    id integer auto_increment primary key,
    numero integer not null,
    aeroporto_id integer,
    foreign key (aeroporto_id) references aeroporto(id),
    qtd_lojas integer,
    version integer
);

CREATE TABLE portao (
    id integer auto_increment primary key,
    numero integer not null,
    aeroporto_id integer,
    foreign key (aeroporto_id) references aeroporto(id),
    terminal_id integer,
    foreign key (terminal_id) references terminal(id),
    aviao varchar(30),
    version integer
);

INSERT INTO aeroporto (codigo, endereco, nome, qtd_pistas, qtd_companhias, version)
VALUES ('CGH', 'Av. Washington Luis - Vila Congonhas, Sao Paulo - SP', 'Aeroporto de Sao Paulo/Congonhas', 10, 25, 0);

