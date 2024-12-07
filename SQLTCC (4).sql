USE master 
DROP DATABASE CrudTcc

CREATE DATABASE CrudTcc
GO 
USE CrudTcc

CREATE TABLE Categoria (
    categoria VARCHAR(255) PRIMARY KEY
);
GO 
INSERT INTO Categoria (categoria) VALUES ('Cabelos');
INSERT INTO Categoria (categoria) VALUES ('Higiene Pessoal');
GO

CREATE TABLE Produto (
    codigo INT NOT NULL PRIMARY KEY,
    nome VARCHAR(60) NOT NULL,
    dataValidade VARCHAR(12) NOT NULL,
    status VARCHAR(20) NOT NULL,
    marca VARCHAR(10) NOT NULL,
    descricao VARCHAR(200) NOT NULL,
    categoria VARCHAR(255) NOT NULL,
    FOREIGN KEY (categoria) REFERENCES Categoria(categoria)
);

GO
CREATE PROCEDURE GerenciarProduto
    @op VARCHAR(10),
    @codigo INT,
    @nome VARCHAR(60),
    @dataValidade VARCHAR(10), -- Formato esperado: 'yyyy-MM-dd'
    @status VARCHAR(20) OUTPUT,
    @marca VARCHAR(10),
    @descricao VARCHAR(200),
    @categoria VARCHAR(255),
    @saida VARCHAR(100) OUTPUT
AS
BEGIN
    -- Verificar se todos os campos obrigatórios estão preenchidos
    IF (@op IS NULL OR @op = '') OR
       (@codigo IS NULL) OR
       (@nome IS NULL OR @nome = '') OR
       (@dataValidade IS NULL OR @dataValidade = '') OR
       (@marca IS NULL OR @marca = '') OR
       (@descricao IS NULL OR @descricao = '') OR
       (@categoria IS NULL OR @categoria = '')
    BEGIN
        SET @saida = 'Campos obrigatórios não preenchidos';
        RETURN;
    END

    -- Variável para a data atual
    DECLARE @dataAtual DATE = GETDATE();

    -- Tentar converter a string para o tipo DATE
    DECLARE @dataValidadeDate DATE;
    BEGIN TRY
        SET @dataValidadeDate = CONVERT(DATE, @dataValidade, 120); -- 120 é o estilo para 'yyyy-MM-dd'
    END TRY
    BEGIN CATCH
        SET @saida = 'Formato de data inválido. Use o formato yyyy-MM-dd.';
        RETURN;
    END CATCH

    -- Verificar se a data de validade é maior que a data atual
    IF @dataValidadeDate <= @dataAtual
    BEGIN
        SET @saida = 'A data de validade deve ser maior que a data atual';
        RETURN;
    END

    -- Verificar a quantidade no estoque
    DECLARE @quantidadeEstoque INT;
    SELECT @quantidadeEstoque = quantidade 
    FROM Estoque 
    WHERE produto = @codigo;

    -- Ajustar o status com base na quantidade no estoque
    IF @quantidadeEstoque IS NULL
    BEGIN
        SET @status = 'Não Registrado'; -- Produto não encontrado no estoque
    END
    ELSE IF @quantidadeEstoque > 3
    BEGIN
        SET @status = 'Disponível';
    END
    ELSE
    BEGIN
        SET @status = 'Indisponível';
    END

    -- Inserir ou atualizar o produto com o status ajustado
    IF @op = 'I'
    BEGIN
        INSERT INTO Produto (codigo, nome, dataValidade, status, marca, descricao, categoria)
        VALUES (@codigo, @nome, @dataValidadeDate, @status, @marca, @descricao, @categoria);
        SET @saida = 'Produto cadastrado com sucesso';
    END
    ELSE IF @op = 'U'
    BEGIN
        UPDATE Produto
        SET nome = @nome, dataValidade = @dataValidadeDate, status = @status, marca = @marca,
            descricao = @descricao, categoria = @categoria
        WHERE codigo = @codigo;
        SET @saida = 'Produto alterado com sucesso!';
    END
    ELSE
    BEGIN
        SET @saida = 'Operação não reconhecida';
    END
END


GO 
CREATE TABLE GerarLucro (
    produto INT NOT NULL,
    precoCompra DECIMAL(10, 2) NOT NULL,
    margemLucro INT NOT NULL,
    precoVenda DECIMAL(10, 2) 
	FOREIGN KEY (produto) REFERENCES Produto(codigo)

);

GO
CREATE PROCEDURE CalcularPrecoVenda
    @precoCompra DECIMAL(10, 2),
    @margemLucro INT, 
    @precoVenda DECIMAL(10, 2) OUTPUT,
    @saida VARCHAR(100) OUTPUT
AS
BEGIN
    -- Ensure @precoCompra is valid
    IF @precoCompra < 0
    BEGIN
        SET @saida = 'O preço de compra deve ser um valor positivo.';
        RETURN;
    END
    
    -- Ensure @margemLucro is within a valid range (0-100)
    IF @margemLucro < 0 OR @margemLucro > 100
    BEGIN
        SET @saida = 'A margem de lucro deve estar entre 0 e 100.';
        RETURN;
    END

    -- Calculate the selling price based on the profit margin percentage
    BEGIN TRY
        SET @precoVenda = @precoCompra * (1 + @margemLucro / 100.0);

        SET @saida = 'Cálculo realizado com sucesso.';
    END TRY
    BEGIN CATCH
        SET @saida = 'Erro ao calcular o preço de venda: ' + ERROR_MESSAGE();
    END CATCH
END;

GO

CREATE PROCEDURE GerenciarGerarLucro
    @produto INT,
    @precoCompra DECIMAL(10, 2),
    @margemLucro INT,
    @precoVenda DECIMAL(10, 2),
    @saida VARCHAR(100) OUTPUT
AS
BEGIN
    -- Verificar se o código do produto é válido
    IF @produto IS NULL OR @produto <= 0
    BEGIN
        SET @saida = 'O código do produto deve ser um valor positivo.';
        RETURN;
    END

    -- Garantir que o preço de compra é válido
    IF @precoCompra < 0
    BEGIN
        SET @saida = 'O preço de compra deve ser um valor positivo.';
        RETURN;
    END
    
    -- Garantir que o preço de venda é válido ou nulo
    IF @precoVenda IS NOT NULL AND @precoVenda < 0
    BEGIN
        SET @saida = 'O preço de venda deve ser um valor positivo ou nulo.';
        RETURN;
    END

    -- Verificar se o produto já existe na tabela GerarLucro
    IF EXISTS (SELECT 1 FROM GerarLucro WHERE produto = @produto)
    BEGIN
        -- Atualizar o registro existente
        BEGIN TRY
            UPDATE GerarLucro
            SET precoCompra = @precoCompra, margemLucro = @margemLucro, precoVenda = @precoVenda
            WHERE produto = @produto;

            SET @saida = 'Registro atualizado com sucesso!';
        END TRY
        BEGIN CATCH
            SET @saida = 'Erro ao atualizar o registro: ' + ERROR_MESSAGE();
            RETURN;
        END CATCH
    END
    ELSE
    BEGIN
        -- Inserir um novo registro
        BEGIN TRY
            INSERT INTO GerarLucro (produto, precoCompra, margemLucro, precoVenda)
            VALUES (@produto, @precoCompra, @margemLucro, @precoVenda);

            SET @saida = 'Registro inserido com sucesso!';
        END TRY
        BEGIN CATCH
            SET @saida = 'Erro ao inserir o registro: ' + ERROR_MESSAGE();
            RETURN;
        END CATCH
    END
END;
GO
CREATE TABLE Estoque (
    produto INT NOT NULL,
    quantidade INT NOT NULL,
    quantidadeMin INT NOT NULL,
	quantidadeTot INT NOT NULL,
	FOREIGN KEY (produto) REFERENCES Produto(codigo)

);
GO
CREATE FUNCTION dbo.fn_produtos()
RETURNS TABLE
AS
RETURN
(
    SELECT 
        P.codigo AS CodigoProduto,
        P.nome AS NomeProduto, 
        P.descricao AS DescricaoProduto,
        P.dataValidade AS DataValidade,
        CASE 
            WHEN E.quantidade IS NULL THEN 'Não Registrado' 
            WHEN E.quantidade = 0 THEN 'Indisponível'
            WHEN E.quantidade < E.quantidadeMin AND E.quantidade > 0 THEN 'Quantidade Mínima'
            ELSE 'Disponível'
        END AS StatusProduto,
        P.marca AS MarcaProduto,
        P.categoria AS CategoriaProduto,
        COALESCE(GL.precoVenda, 0.00) AS PrecoVenda
    FROM Produto P
    LEFT JOIN GerarLucro GL ON P.codigo = GL.produto
    LEFT JOIN Estoque E ON P.codigo = E.produto
);

GO
CREATE  PROCEDURE GerenciarEstoque
    @op VARCHAR(10), 
    @produto INT,
    @quantidade INT,
    @quantidadeMin INT,
    @saida VARCHAR(100) OUTPUT
AS
BEGIN
    -- Inicializar o parâmetro de saída
    SET @saida = '';

    -- Verificar se os campos obrigatórios estão preenchidos
   IF ( @op IS NULL OR LTRIM(RTRIM(@op)) = '' ) OR
       ( @produto IS NULL OR @produto <= 0 OR @produto = '') OR
       ( @quantidade IS NULL OR @quantidade < 0  OR @quantidade = '' ) OR
       ( @quantidadeMin IS NULL OR @quantidadeMin < 0  OR @quantidadeMin = '')
    BEGIN
        SET @saida = 'Campos obrigatórios não preenchidos.';
        RETURN;
    END
    -- Verificar se os dados são válidos
    IF @produto <= 0
    BEGIN
        SET @saida = 'O código do produto deve ser um valor positivo.';
        RETURN;
    END

    IF @quantidade < 0 OR @quantidadeMin < 0
    BEGIN
        SET @saida = 'As quantidades devem ser valores positivas.';
        RETURN;
    END
   
    -- Inserir novo registro no estoque
    IF @op = 'I'
    BEGIN
        BEGIN TRY
            -- Verificar se o produto já existe no estoque
            IF EXISTS (SELECT 1 FROM Estoque WHERE produto = @produto)
            BEGIN
                SET @saida = 'Produto já existe no estoque. Use a operação de atualização para modificar o estoque.';
                RETURN;
            END

            -- Inserir novo registro
            INSERT INTO Estoque (produto, quantidade, quantidadeMin, quantidadeTot)
            VALUES (@produto, @quantidade, @quantidadeMin, 0);

            -- Atualizar a quantidade total de todos os produtos
            UPDATE Estoque
            SET quantidadeTot = (SELECT SUM(quantidade) FROM Estoque);

            -- Atualizar o status do produto com base na quantidade
            IF @quantidade > @quantidadeMin
            BEGIN
                UPDATE Produto
                SET status = 'Disponível'
                WHERE codigo = @produto;
            END
            ELSE
            BEGIN
                UPDATE Produto
                SET status = 'Quantidade Minima'
                WHERE codigo = @produto;
            END

            SET @saida = 'Estoque cadastrado com sucesso!';
        END TRY
        BEGIN CATCH
            SET @saida = 'Erro ao cadastrar estoque: ' + ERROR_MESSAGE();
        END CATCH
    END
    -- Atualizar o estoque existente
    ELSE IF @op = 'U'
    BEGIN
        BEGIN TRY
            -- Verificar se o produto já existe no estoque
            IF NOT EXISTS (SELECT 1 FROM Estoque WHERE produto = @produto)
            BEGIN
                SET @saida = 'Produto não encontrado no estoque. Use a operação de inserção para adicionar o produto.';
                RETURN;
            END

            -- Atualizar o registro existente
            UPDATE Estoque
            SET quantidade = @quantidade, quantidadeMin = @quantidadeMin
            WHERE produto = @produto;

            -- Atualizar a quantidade total de todos os produtos
            UPDATE Estoque
            SET quantidadeTot = (SELECT SUM(quantidade) FROM Estoque);

            -- Atualizar o status do produto com base na quantidade
            IF @quantidade > @quantidadeMin
            BEGIN
                UPDATE Produto
                SET status = 'Disponível'
                WHERE codigo = @produto;
            END
            ELSE
            BEGIN
                UPDATE Produto
                SET status = 'Quantidade Minima'
                WHERE codigo = @produto;
            END

            SET @saida = 'Estoque alterado com sucesso!';
        END TRY
        BEGIN CATCH
            SET @saida = 'Erro ao alterar estoque: ' + ERROR_MESSAGE();
        END CATCH
    END
    -- Operação não reconhecida
    ELSE
    BEGIN
        SET @saida = 'Operação não reconhecida. Use "I" para inserir ou "U" para atualizar.';
    END
END;

GO

CREATE FUNCTION dbo.fn_produtosE()
RETURNS TABLE
AS
RETURN
(
    SELECT 
        P.codigo AS CodigoProduto,
        P.nome AS NomeProduto, 
        P.marca AS MarcaProduto,
        P.categoria AS CategoriaProduto,
		e.quantidadeMin AS QuantidadeMin,
        e.quantidade AS Quantidade
    FROM Produto P
    LEFT JOIN Estoque e ON P.codigo = e.produto
);


GO

CREATE FUNCTION dbo.fn_produtosPorCodigo
(
    @codigoProduto INT
)
RETURNS TABLE
AS
RETURN
(
    SELECT 
        P.codigo AS CodigoProduto,
        P.nome AS NomeProduto, 
        P.marca AS MarcaProduto,
        P.categoria AS CategoriaProduto,
		e.quantidadeMin AS QuantidadeMin,
        e.quantidade AS Quantidade
    FROM Produto P
    LEFT JOIN Estoque e ON P.codigo = e.produto
    WHERE P.codigo = @codigoProduto
);
GO
CREATE FUNCTION dbo.fn_produtosPorCategoria
(
    @categoriaProduto VARCHAR(255)
)
RETURNS TABLE
AS
RETURN
(
    SELECT 
        P.codigo AS CodigoProduto,
        P.nome AS NomeProduto, 
        P.marca AS MarcaProduto,
        P.categoria AS CategoriaProduto,
		e.quantidadeMin AS QuantidadeMin,
        e.quantidade AS Quantidade
    FROM Produto P
    LEFT JOIN Estoque e ON P.codigo = e.produto
    WHERE LOWER(LTRIM(RTRIM(P.categoria))) = LOWER(LTRIM(RTRIM(@categoriaProduto)))
);

GO

CREATE TABLE Itens (
    produto INT NOT NULL PRIMARY KEY,
    quantidade INT NOT NULL,
    FOREIGN KEY (produto) REFERENCES Produto(codigo)
);


GO

CREATE PROCEDURE GerenciarItens
    @op VARCHAR(10),          -- Operação (I para inserção/atualização)
    @produto INT,             -- Código do produto
    @quantidade INT,          -- Quantidade do produto
    @saida VARCHAR(300) OUTPUT -- Mensagem de saída
AS
BEGIN
    -- Inicializar o parâmetro de saída
    SET @saida = '';

    -- Verificar se todos os campos obrigatórios estão preenchidos
    IF @op IS NULL OR LTRIM(RTRIM(@op)) = '' OR 
       @produto IS NULL OR @produto <= 0 OR 
       @quantidade IS NULL OR @quantidade <= 0
    BEGIN
        SET @saida = 'Campos obrigatórios não preenchidos.';
        RETURN;
    END

    -- Verificar se a operação é de inserção ou atualização
    IF @op = 'I'
    BEGIN
        -- Inserir ou atualizar item no estoque
        BEGIN TRY
            -- Verificar se o produto existe no estoque
            IF NOT EXISTS (SELECT 1 FROM Estoque WHERE produto = @produto)
            BEGIN
                SET @saida = 'Produto não encontrado no estoque. Não é possível inserir ou atualizar o item.';
                RETURN;
            END

            -- Verificar a quantidade disponível no estoque
            DECLARE @quantidadeEstoque INT;
            SELECT @quantidadeEstoque = quantidade
            FROM Estoque
            WHERE produto = @produto;

            -- Verificar se o produto já existe nos itens
            IF EXISTS (SELECT 1 FROM Itens WHERE produto = @produto)
            BEGIN
                -- Emitir um aviso se a quantidade disponível em estoque for menor que a quantidade solicitada
                IF @quantidadeEstoque < @quantidade
                BEGIN
                    SET @saida = 'ATENÇÃO: A quantidade disponível em estoque (' + CAST(@quantidadeEstoque AS VARCHAR) + ') é menor que a quantidade desejada (' + CAST(@quantidade AS VARCHAR) + '). Considere ajustar o pedido ou reabastecer o estoque.';
                END
                ELSE
                BEGIN
                    -- Se o produto existir e houver estoque suficiente, atualizar a quantidade
                    UPDATE Itens
                    SET quantidade = @quantidade
                    WHERE produto = @produto;

                    SET @saida = 'Quantidade do item atualizada com sucesso!';
                END
            END
            ELSE
            BEGIN
                -- Se o produto não existir nos itens, verificar se a quantidade disponível no estoque é suficiente
                IF @quantidadeEstoque >= @quantidade
                BEGIN
				 IF NOT EXISTS (SELECT 1 FROM Estoque WHERE produto = @produto)
            BEGIN
                SET @saida = 'Produto não encontrado no estoque. Não é possível inserir ou atualizar o item.';
                RETURN;
            END
                    INSERT INTO Itens (produto, quantidade)
                    VALUES (@produto, @quantidade);

                    SET @saida = 'Item inserido com sucesso!';
                END
                ELSE
                BEGIN
                    SET @saida = 'ATENÇÃO: A quantidade disponível em estoque (' + CAST(@quantidadeEstoque AS VARCHAR) + ') é menor que a quantidade desejada (' + CAST(@quantidade AS VARCHAR) + '). Não é possível inserir o item.';
                END
            END
        END TRY
        BEGIN CATCH
            -- Capturar qualquer erro que ocorra durante a inserção ou atualização
            SET @saida = 'Erro ao inserir ou atualizar item: ' + ERROR_MESSAGE();
        END CATCH
    END
    ELSE
    BEGIN
        SET @saida = 'Operação não reconhecida. Use "I" para inserção/atualização.';
    END
END;


GO
CREATE FUNCTION dbo.fn_produtosPorCodigoI
(
    @codigoProduto INT
)
RETURNS TABLE
AS
RETURN
(
    SELECT 
        P.codigo AS CodigoProduto,
        P.nome AS NomeProduto, 
        P.marca AS MarcaProduto,
        P.categoria AS CategoriaProduto,
		e.quantidade AS QuantidadeE,
        i.quantidade AS Quantidade
    FROM Produto P
    LEFT JOIN Itens i ON P.codigo = i.produto
	LEFT JOIN Estoque e ON P.codigo = e.produto
    WHERE P.codigo = @codigoProduto
);
GO

CREATE FUNCTION dbo.fn_produtosPorCategoriaI
(
    @categoriaProduto VARCHAR(255)
)
RETURNS TABLE
AS
RETURN
(
    SELECT 
        P.codigo AS CodigoProduto,
        P.nome AS NomeProduto, 
        P.marca AS MarcaProduto,
        P.categoria AS CategoriaProduto,
		e.quantidade AS QuantidadeE,
        i.quantidade AS Quantidade
    FROM Produto P
        LEFT JOIN Itens i ON P.codigo = i.produto
			LEFT JOIN Estoque e ON P.codigo = e.produto
    WHERE LOWER(LTRIM(RTRIM(P.categoria))) = LOWER(LTRIM(RTRIM(@categoriaProduto)))
);

GO
CREATE TABLE FormaPagamento (
    formaPagamento VARCHAR(60) NOT NULL PRIMARY KEY,
   
);
    
GO
INSERT INTO FormaPagamento (formaPagamento)
VALUES
    ('Dinheiro'),
    ('Cartão de Crédito'),
    ('Cartão de Débito')
GO

CREATE TABLE Venda (
    codigo INT NOT NULL,
    data DATE NOT NULL, 
    formaPagamento VARCHAR(60) NOT NULL,
    valorTotal DECIMAL(10, 2) NOT NULL,
    subValor DECIMAL(10, 2) NOT NULL,
    item INT NOT NULL, 
	quantidade INT NOT NULL,
	FOREIGN KEY (item) REFERENCES Produto(codigo),
	FOREIGN KEY (formaPagamento) REFERENCES FormaPagamento(formaPagamento)
);
GO

CREATE FUNCTION dbo.fn_vendasPorCodigo(
    @codigoVenda INT
)
RETURNS TABLE
AS
RETURN
(
    SELECT 
        v.codigo AS CodigoVenda,
        v.data AS DataVenda, 
        v.formaPagamento AS PagamentoVenda,
        p.nome AS Produto,
        v.quantidade AS Quantidade,
        g.precoVenda AS PrecoVenda,
        v.subValor AS SubValor,
        v.valorTotal AS ValorTotal
    FROM Venda v 
    LEFT JOIN Produto p ON p.codigo = v.item
    LEFT JOIN GerarLucro g ON g.produto = p.codigo
    WHERE v.codigo = @codigoVenda
);
GO
CREATE FUNCTION dbo.fn_vendasPorData(
    @dataVenda VARCHAR(10) -- Recebe a data como string no formato DD/MM/YYYY
)
RETURNS TABLE
AS
RETURN
(
    SELECT 
        v.codigo AS CodigoVenda,
        v.data AS DataVenda, 
        v.formaPagamento AS PagamentoVenda,
        p.nome AS Produto,
        v.quantidade AS Quantidade,
        g.precoVenda AS PrecoVenda,
        v.subValor AS SubValor,
        v.valorTotal AS ValorTotal
    FROM Venda v 
    LEFT JOIN Produto p ON p.codigo = v.item
    LEFT JOIN GerarLucro g ON g.produto = p.codigo
    WHERE v.data = @dataVenda
);

GO
CREATE FUNCTION dbo.fn_Itens()
RETURNS TABLE
AS
RETURN
(
    SELECT 
        i.produto AS CodigoProduto,
        P.nome AS NomeProduto, 
        P.marca AS MarcaProduto,
        ISNULL(g.precoVenda, 0) AS PrecoVenda,
        ISNULL(i.quantidade, 0) AS Quantidade,
        ISNULL(g.precoVenda * ISNULL(i.quantidade, 0), 0) AS SubValor,
        SUM(ISNULL(g.precoVenda * ISNULL(i.quantidade, 0), 0)) OVER () AS ValorTotal
    FROM Itens i
    LEFT JOIN Produto p ON P.codigo = i.produto
    LEFT JOIN GerarLucro g ON P.codigo = g.produto
);

GO
CREATE FUNCTION dbo.fn_vendas()
RETURNS TABLE
AS
RETURN
(
    SELECT 
        v.codigo AS CodigoVenda,
        v.data AS DataVenda, 
        v.formaPagamento AS PagamentoVenda,
        P.nome AS Produto,
        v.quantidade AS Quantidade,
        g.precoVenda AS PrecoVenda,
        v.subValor AS SubValor,
        v.valorTotal AS ValorTotal,
        ROW_NUMBER() OVER (ORDER BY v.codigo ASC) AS RowNum
    FROM Venda v 
    LEFT JOIN Produto p ON p.codigo = v.item
    LEFT JOIN GerarLucro g ON g.produto = p.codigo
);


GO
CREATE PROCEDURE InserirVenda
    @op VARCHAR(10),             
    @codigo INT = NULL,           
    @dataVenda VARCHAR(10) = NULL,
    @formaPagamento VARCHAR(60) = NULL, 
    @subValor DECIMAL(10, 2) = NULL,    
    @item INT = NULL,             
    @quantidade INT = NULL,       
    @saida VARCHAR(100) OUTPUT    
AS
BEGIN
    SET @saida = '';

    IF @op IS NULL OR LTRIM(RTRIM(@op)) = ''
    BEGIN
        SET @saida = 'Erro: A operação deve ser especificada.';
        RETURN;
    END

    IF @op = 'I'
    BEGIN
        -- Verificação de campos obrigatórios para inserção
        IF @dataVenda IS NULL OR 
           @codigo IS NULL OR @codigo <= 0 OR 
           @formaPagamento IS NULL  OR 
           @subValor IS NULL OR @subValor <= 0 OR 
           @quantidade IS NULL OR @quantidade <= 0 OR 
           @item IS NULL OR @item <= 0
        BEGIN
            SET @saida = 'Campos obrigatórios não preenchidos.';
            RETURN;
        END

        BEGIN TRY
            BEGIN TRANSACTION;

            -- Inserir a venda
            INSERT INTO Venda (codigo, data, formaPagamento, subValor, item, quantidade, valorTotal)
            VALUES (@codigo, @dataVenda, @formaPagamento, @subValor, @item, @quantidade, 0);

            -- Verificar se a inserção foi bem-sucedida
            IF @@ROWCOUNT = 0
            BEGIN
                SET @saida = 'Erro ao inserir venda.';
                ROLLBACK;
                RETURN;
            END

            -- Atualizar o valor total da venda
            UPDATE Venda
            SET valorTotal = (
                SELECT SUM(subValor)
                FROM Venda
                WHERE codigo = @codigo
            )
            WHERE codigo = @codigo;

            -- Verificar se o item existe e atualizar estoque
            IF EXISTS (SELECT 1 FROM Itens WHERE produto = @item)
            BEGIN
                DELETE FROM Itens
                WHERE produto = @item;

                -- Atualizar a quantidade no estoque
                UPDATE Estoque
                SET quantidade = quantidade - @quantidade
                WHERE produto = @item;

                COMMIT;
                SET @saida = 'Erro: Item não encontrado.';
            END
            ELSE
            BEGIN
                SET @saida =  'Venda finalizada com sucesso!';
                ROLLBACK;
                RETURN;
            END
        END TRY
        BEGIN CATCH
            ROLLBACK;
            SET @saida = 'Erro ao inserir venda ou remover item: ' + ERROR_MESSAGE();
        END CATCH
    END
    ELSE IF @op = 'D'
    BEGIN
        -- Verificação para exclusão
        IF @item IS NULL OR @item <= 0
        BEGIN
            SET @saida = 'Erro: Código do item é necessário para exclusão.';
            RETURN;
        END

        BEGIN TRY
            BEGIN TRANSACTION;

            IF EXISTS (SELECT 1 FROM Itens WHERE produto = @item)
            BEGIN
                DELETE FROM Itens
                WHERE produto = @item;

                COMMIT;
                SET @saida = 'Item removido com sucesso!';
            END
            ELSE
            BEGIN
                SET @saida = 'Erro: Item não encontrado na venda.';
                ROLLBACK;
                RETURN;
            END
        END TRY
        BEGIN CATCH
            ROLLBACK;
            SET @saida = 'Erro ao remover item: ' + ERROR_MESSAGE();
        END CATCH
    END
    ELSE
    BEGIN
        SET @saida = 'Operação não reconhecida. Use "I" para inserir ou "D" para excluir.';
    END
END;

GO 
CREATE PROCEDURE ExcluirItemVenda
    @op VARCHAR(10), -- Operação que deve ser 'D' para exclusão
    @item INT,       -- Código do item a ser excluído
    @saida VARCHAR(100) OUTPUT -- Mensagem de saída
AS
BEGIN
    -- Inicializar o parâmetro de saída
    SET @saida = '';

    -- Verificar se a operação é de exclusão
    IF @op <> 'D'
    BEGIN
        SET @saida = 'Operação não reconhecida. Use "D" para exclusão.';
        RETURN;
    END

    -- Verificar se o item foi passado
    IF @item IS NULL
    BEGIN
        SET @saida = 'Erro: Código do item é necessário para exclusão.';
        RETURN;
    END

    -- Iniciar uma transação para exclusão
    BEGIN TRY
        BEGIN TRANSACTION;

        -- Verificar se o item existe na tabela antes de deletar
        IF EXISTS (SELECT 1 FROM Itens WHERE produto = @item)
        BEGIN
            -- Remover o item da tabela de Itens
            DELETE FROM Itens
            WHERE produto = @item;

            COMMIT;

            -- Mensagem de sucesso
            SET @saida = 'Item removido com sucesso.';
        END
        ELSE
        BEGIN
            -- Se o item não existir, retornar mensagem de erro
            SET @saida = 'Erro: Item não encontrado.';
            ROLLBACK;
            RETURN;
        END
    END TRY
    BEGIN CATCH
        -- Em caso de erro, reverter a transação
        ROLLBACK;

        -- Capturar o erro e definir a mensagem de saída
        SET @saida = 'Erro ao excluir item: ' + ERROR_MESSAGE();
    END CATCH
END;
GO
CREATE TABLE Demanda (
    produto INT NOT NULL,
    mes INT NOT NULL,
    totalVendas INT NOT NULL,
    demanda INT NOT NULL,
	ano INT NOT NULL,
    FOREIGN KEY (produto) REFERENCES Produto(codigo) 
);
GO

CREATE PROCEDURE ConsultarSomaVendasUltimosTresMesesProduto
    @mesAtual INT,
    @codigoProduto INT,
    @mensagemSaida VARCHAR(255) OUTPUT  -- Parâmetro de saída para mensagens
AS
BEGIN
    -- Variáveis para calcular as datas de início e fim dos três meses anteriores
    DECLARE @dataInicio DATETIME;
    DECLARE @dataFim DATETIME;
    DECLARE @anoAtual INT;
    DECLARE @vendaMesAtual INT;
    DECLARE @mesesAnteriores INT = 3;

    -- Obter o ano atual
    SET @anoAtual = YEAR(GETDATE());

    -- Definir o primeiro dia do mês atual
    SET @dataFim = DATEFROMPARTS(@anoAtual, @mesAtual, 1);

    -- Calcular o primeiro dia de três meses antes
    SET @dataInicio = DATEADD(MONTH, -3, @dataFim);

    -- Mensagem inicial
    SET @mensagemSaida = 'Consultando vendas para o produto ' + CAST(@codigoProduto AS VARCHAR) + ' no mês ' + CAST(@mesAtual AS VARCHAR) + ' do ano ' + CAST(@anoAtual AS VARCHAR);

    -- Verificar se já existe um registro para o produto, mês e ano atuais
    IF EXISTS (SELECT 1 FROM Demanda WHERE produto = @codigoProduto AND mes = @mesAtual AND ano = @anoAtual)
    BEGIN
        -- Se existir, fazer apenas o UPDATE
        SET @mensagemSaida = 'Registro existente encontrado. Atualizando totalVendas e demanda.';

        UPDATE Demanda
        SET totalVendas = (SELECT ISNULL(SUM(quantidade), 0) -- Garante que não seja NULL
                           FROM Venda
                           WHERE data >= @dataInicio
                             AND data < @dataFim
                             AND item = @codigoProduto),
            demanda = CASE 
                         WHEN totalVendas > 0 THEN totalVendas / @mesesAnteriores 
                         ELSE 0 
                      END
        WHERE produto = @codigoProduto AND mes = @mesAtual AND ano = @anoAtual;

		UPDATE Demanda
    SET demanda = CASE 
                     WHEN totalVendas > 0 THEN totalVendas / @mesesAnteriores 
                     ELSE 0 
                  END
    WHERE produto = @codigoProduto AND mes = @mesAtual AND ano = @anoAtual;

        SET @mensagemSaida = 'Previsão de Demanda:';
    END
    ELSE
    BEGIN
        -- Se não existir, fazer o INSERT
        SET @mensagemSaida = 'Nenhum registro encontrado. Inserindo novo registro.';

        -- Verificar se há vendas registradas para o produto no mês atual
        SET @vendaMesAtual = (SELECT ISNULL(SUM(quantidade), 0)
                              FROM Venda
                              WHERE data >= @dataInicio
                                AND data < DATEADD(MONTH, 1, @dataFim)  -- Fim do mês
                                AND item = @codigoProduto);

        -- Se houver vendas no mês atual, inserir na tabela de demanda
        IF @vendaMesAtual > 0
        BEGIN
            INSERT INTO Demanda (produto, mes, totalVendas, demanda, ano)
            VALUES (@codigoProduto, 
                    @mesAtual,  
                    (SELECT ISNULL(SUM(quantidade), 0)  -- Garante que não seja NULL
                     FROM Venda
                     WHERE data >= @dataInicio
                       AND data < @dataFim
                       AND item = @codigoProduto), 
                    0, @anoAtual);

            SET @mensagemSaida = 'Inserção concluída. Atualizando demanda.';

            -- Após o INSERT, atualizar a demanda
            UPDATE Demanda
            SET demanda = (SELECT CASE 
                                     WHEN totalVendas > 0 THEN totalVendas / @mesesAnteriores 
                                     ELSE 0 
                                  END
                           FROM Demanda
                           WHERE produto = @codigoProduto AND mes = @mesAtual AND ano = @anoAtual);

            SET @mensagemSaida = 'Previsão de Demanda:';
        END
        ELSE
        BEGIN
            SET @mensagemSaida = 'Nenhuma venda encontrada no mês atual para o produto. Nenhum registro inserido.';
        END
    END
END;

GO


CREATE FUNCTION dbo.ObterVendasUltimosTresMesesComNome (
    @mesAtual INT,        -- Mês de entrada
    @codigoProduto INT    -- Código do produto
)
RETURNS @Resultado TABLE (
    produto INT,
    nome NVARCHAR(255),
    mes INT,
    nomeMes NVARCHAR(20),
    quantidadeVendas INT,
    totalVendas INT,
    demanda INT
)
AS
BEGIN
    DECLARE @dataInicio DATETIME;
    DECLARE @dataFim DATETIME;
    DECLARE @anoAtual INT;
    
    -- Obter o ano atual
    SET @anoAtual = YEAR(GETDATE());

    -- Definir o primeiro dia do mês atual
    SET @dataFim = DATEFROMPARTS(@anoAtual, @mesAtual, 1);

    -- Calcular o primeiro dia de três meses antes
    SET @dataInicio = DATEADD(MONTH, -3, @dataFim);

    -- Inserir os resultados na tabela de retorno
    INSERT INTO @Resultado (produto, nome, mes, nomeMes, quantidadeVendas, totalVendas, demanda)
    SELECT 
        p.codigo AS produto,
        p.nome AS nome,
        MONTH(v.data) AS mes,
        CASE 
            WHEN MONTH(v.data) = 1 THEN 'Janeiro'
            WHEN MONTH(v.data) = 2 THEN 'Fevereiro'
            WHEN MONTH(v.data) = 3 THEN 'Março'
            WHEN MONTH(v.data) = 4 THEN 'Abril'
            WHEN MONTH(v.data) = 5 THEN 'Maio'
            WHEN MONTH(v.data) = 6 THEN 'Junho'
            WHEN MONTH(v.data) = 7 THEN 'Julho'
            WHEN MONTH(v.data) = 8 THEN 'Agosto'
            WHEN MONTH(v.data) = 9 THEN 'Setembro'
            WHEN MONTH(v.data) = 10 THEN 'Outubro'
            WHEN MONTH(v.data) = 11 THEN 'Novembro'
            WHEN MONTH(v.data) = 12 THEN 'Dezembro'
        END AS nomeMes,
        ISNULL(SUM(CASE 
                      WHEN v.data >= @dataInicio AND v.data < @dataFim 
                      THEN v.quantidade 
                      ELSE 0 
                   END), 0) AS quantidadeVendas,
        d.totalVendas,
        d.demanda
    FROM Produto p
    LEFT JOIN Venda v ON v.item = p.codigo
    LEFT JOIN Demanda d ON d.produto = p.codigo AND d.mes = @mesAtual AND d.ano = @anoAtual
     WHERE v.data >= @dataInicio
      AND v.data < @dataFim
      AND p.codigo = @codigoProduto
    GROUP BY p.codigo, p.nome, MONTH(v.data), d.totalVendas, d.demanda
    ORDER BY mes DESC;

    RETURN;
END;


GO 
CREATE PROCEDURE ProdutoMaisVendido
    @idMes INT, -- ID do mês a ser consultado
    @saida VARCHAR(100) OUTPUT -- Mensagem de saída
AS
BEGIN
    -- Inicializar a saída
    SET @saida = '';

    -- Verificar se o mês é válido
    IF @idMes < 1 OR @idMes > 12
    BEGIN
        SET @saida = 'Erro: ID do mês inválido.';
        RETURN;
    END

    BEGIN TRY
        -- Verificar se existem vendas no mês especificado
        IF NOT EXISTS (
            SELECT 1
            FROM Venda v
            WHERE MONTH(v.data) = @idMes AND YEAR(v.data) = YEAR(GETDATE())
        )
        BEGIN
            SET @saida = 'Nenhuma venda encontrada para o mês especificado.';
            RETURN;
        END

        -- Consultar todos os produtos vendidos no mês especificado e no ano atual, ordenados por quantidade descendente
        SELECT p.nome, SUM(v.quantidade) AS quantidadeVendida
        FROM Venda v
        JOIN Produto p ON v.item = p.codigo
        WHERE MONTH(v.data) = @idMes AND YEAR(v.data) = YEAR(GETDATE())
        GROUP BY p.nome
        ORDER BY quantidadeVendida DESC;

        -- Definir mensagem de sucesso
        SET @saida = 'Consulta realizada com sucesso!';
    END TRY
    BEGIN CATCH
        -- Capturar o erro e definir mensagem de saída
        SET @saida = 'Erro ao consultar o produto: ' + ERROR_MESSAGE();
    END CATCH
END;

-- Inserting a product with category 'Cabelos' and status "Não Registrado"
INSERT INTO Produto (codigo, nome, dataValidade, marca, descricao, categoria, status)
VALUES (2, 'Shampoo Anticaspa', '2025-12-31', 'Marca A', 'Shampoo para cabelos com fórmula anticaspa', 'Cabelos', 'Não Registrado');

-- Inserting a product with category 'Higiene pessoal' and status "Não Registrado"
INSERT INTO Produto (codigo, nome, dataValidade, marca, descricao, categoria, status)
VALUES (3, 'Sabonete Líquido', '2024-10-31', 'Marca B', 'Sabonete líquido para higiene pessoal', 'Higiene Pessoal', 'Não Registrado');

-- Inserting another product with category 'Cabelos' and status "Não Registrado"
INSERT INTO Produto (codigo, nome, dataValidade, marca, descricao, categoria, status)
VALUES (4, 'Condicionador Hidratante', '2025-11-30', 'Marca C', 'Condicionador hidratante para cabelos secos', 'Cabelos', 'Não Registrado');

-- Inserting another product with category 'Higiene pessoal' and status "Não Registrado"
INSERT INTO Produto (codigo, nome, dataValidade, marca, descricao, categoria, status)
VALUES (5, 'Creme Dental', '2024-09-15', 'Marca D', 'Creme dental para uso diário', 'Higiene Pessoal', 'Não Registrado');


select * from venda