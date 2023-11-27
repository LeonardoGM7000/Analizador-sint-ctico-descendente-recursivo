import java.util.List;

public class ASDR implements Parser{

    private int i = 0;
    private boolean hayErrores = false;
    private Token preanalisis;
    private final List<Token> tokens;


    public ASDR(List<Token> tokens){
        this.tokens = tokens;
        preanalisis = this.tokens.get(i);
    }

    @Override
    public boolean parse() {
        PROGRAM();

        if(preanalisis.tipo == TipoToken.EOF && !hayErrores){
            System.out.println("Todo bien");
            return  true;
        }else {
            System.out.println("Codea bien, inutil");
        }
        return false;
    }

    // PROGRAM -> DECLARATION
    private void PROGRAM(){
        DECLARATION();
    }

    // DECLARATION -> FUN_DECL DECLARATION | VAR_DECL DECLARATION | STATEMENT DECLARATION | /e
    private void DECLARATION(){
        if(hayErrores)
            return;

        if(preanalisis.tipo == TipoToken.FUN){
            FUN_DECL();
            DECLARATION();
        }
        else if (preanalisis.tipo == TipoToken.VAR) {
            VAR_DECL();
            DECLARATION();
        }
        else if ( preanalisis.tipo == TipoToken.BANG || 
        		  preanalisis.tipo == TipoToken.MINUS || 
        		  preanalisis.tipo == TipoToken.TRUE || 
        		  preanalisis.tipo == TipoToken.FALSE || 
        		  preanalisis.tipo == TipoToken.NULL || 
        		  preanalisis.tipo == TipoToken.NUMBER || 
        		  preanalisis.tipo == TipoToken.STRING || 
        		  preanalisis.tipo == TipoToken.IDENTIFIER || 
        		  preanalisis.tipo == TipoToken.LEFT_PAREN || 
        		  preanalisis.tipo == TipoToken.FOR || 
        		  preanalisis.tipo == TipoToken.IF || 
        		  preanalisis.tipo == TipoToken.PRINT || 
        		  preanalisis.tipo == TipoToken.RETURN || 
        		  preanalisis.tipo == TipoToken.WHILE || 
        		  preanalisis.tipo == TipoToken.LEFT_BRACE ) {
            STATEMENT();
            DECLARATION();
        }
    }
    //FUN_DECL -> fun FUNCTION
    private void FUN_DECL(){
    	if(hayErrores)
            return;

        if(preanalisis.tipo == TipoToken.FUN){
            match(TipoToken.FUN);
            FUNCTION();
        }
        else{
            hayErrores = true;
            System.out.println("Se esperaba 'fun'");
        }
    }
    //VAR_DECL -> var id VAR_INIT ;
    private void VAR_DECL(){
    	if(hayErrores)
            return;

        if(preanalisis.tipo == TipoToken.VAR){
            match(TipoToken.VAR);
            match(TipoToken.IDENTIFIER);
            VAR_INIT();
            match(TipoToken.SEMICOLON);
        }
        else{
            hayErrores = true;
            System.out.println("Se esperaba 'var'");
        }
    }
    //VAR_INIT -> = EXPRESSION | /e
    private void VAR_INIT(){
    	if(hayErrores)
            return;

        if(preanalisis.tipo == TipoToken.EQUAL){
            match(TipoToken.EQUAL);
            EXPRESSION();
        }
    }
    // STATEMENT -> EXPR_STMT | FOR_STMT | IF_STMT | PRINT_STMT | RETURN_STMT | WHILE_STMT | BLOCK
    
    private void STATEMENT(){
        if(hayErrores)
            return;

        if(preanalisis.tipo == TipoToken.BANG || 
        	preanalisis.tipo == TipoToken.MINUS || 
        	preanalisis.tipo == TipoToken.TRUE || 
        	preanalisis.tipo == TipoToken.FALSE || 
        	preanalisis.tipo == TipoToken.NULL || 
        	preanalisis.tipo == TipoToken.NUMBER || 
        	preanalisis.tipo == TipoToken.STRING || 
        	preanalisis.tipo == TipoToken.IDENTIFIER || 
        	preanalisis.tipo == TipoToken.LEFT_PAREN){
            EXPR_STMT();
        }
        else if (preanalisis.tipo == TipoToken.FOR) {
            FOR_STMT();
        }
        else if ( preanalisis.tipo == TipoToken.IF ) {
            IF_STMT();
        }
        else if ( preanalisis.tipo == TipoToken.PRINT ) {
            PRINT_STMT();
        }
        else if ( preanalisis.tipo == TipoToken.RETURN ) {
            RETURN_STMT();
        }
        else if ( preanalisis.tipo == TipoToken.WHILE ) {
            WHILE_STMT();
        }
        else if ( preanalisis.tipo == TipoToken.LEFT_BRACE ) {
            BLOCK();
        }
        else{
            hayErrores = true;
            System.out.println("Se esperaba '!' or '-' or 'true' or 'false' or 'null' or 'number' or 'string' or 'id' or '(' or 'for' or 'if' or 'print' or 'return' or 'while' or '{' ");
        }
    }
    //EXPR_STMT -> EXPRESSION ;
    private void EXPR_STMT(){
    	if(hayErrores)
            return;

        if(preanalisis.tipo == TipoToken.BANG || 
        	preanalisis.tipo == TipoToken.MINUS || 
        	preanalisis.tipo == TipoToken.TRUE || 
        	preanalisis.tipo == TipoToken.FALSE || 
        	preanalisis.tipo == TipoToken.NULL || 
        	preanalisis.tipo == TipoToken.NUMBER || 
        	preanalisis.tipo == TipoToken.STRING || 
        	preanalisis.tipo == TipoToken.IDENTIFIER || 
        	preanalisis.tipo == TipoToken.LEFT_PAREN){
            EXPRESSION();
        	match(TipoToken.SEMICOLON);
        }
        else{
            hayErrores = true;
            System.out.println("Se esperaba '!' or '-' or 'true' or 'false' or 'null' or 'number' or 'string' or 'id' or '(' ");
        }
    }
    //FOR_STMT -> for ( FOR_STMT_1 FOR_STMT_2 FOR_STMT_3 ) STATEMENT
    private void FOR_STMT(){
    	if(hayErrores)
            return;

        if(preanalisis.tipo == TipoToken.FOR){
            match(TipoToken.FOR);
            match(TipoToken.LEFT_PAREN);
            FOR_STMT_1();
            FOR_STMT_2(); 
            FOR_STMT_3();
            match(TipoToken.RIGHT_PAREN);
            STATEMENT();
        }
        else{
            hayErrores = true;
            System.out.println("Se esperaba 'for' ");
        }
    }
    //FOR_STMT_1 -> VAR_DECL | EXPR_STMT | ;
    private void FOR_STMT_1(){
    	if(hayErrores)
            return;

        if(preanalisis.tipo == TipoToken.VAR){
            VAR_DECL();
        }
        else if(preanalisis.tipo == TipoToken.BANG || 
        		preanalisis.tipo == TipoToken.MINUS || 
        		preanalisis.tipo == TipoToken.TRUE || 
        		preanalisis.tipo == TipoToken.FALSE || 
        		preanalisis.tipo == TipoToken.NULL || 
        		preanalisis.tipo == TipoToken.NUMBER || 
        		preanalisis.tipo == TipoToken.STRING || 
        		preanalisis.tipo == TipoToken.IDENTIFIER || 
        		preanalisis.tipo == TipoToken.LEFT_PAREN){
            EXPR_STMT();
        }
        else if(preanalisis.tipo == TipoToken.SEMICOLON){
        	match(TipoToken.SEMICOLON);
        }
        else{
            hayErrores = true;
            System.out.println("Se esperaba 'var' or '!' or '-' or 'true' or 'false' or 'null' or 'number' or 'string' or 'id' or '(' or ';' ");
        }
    }
    //FOR_STMT_2 -> EXPRESSION; | ;
    private void FOR_STMT_2(){
    	if(hayErrores)
            return;

        if(preanalisis.tipo == TipoToken.BANG || 
        	preanalisis.tipo == TipoToken.MINUS || 
        	preanalisis.tipo == TipoToken.TRUE || 
        	preanalisis.tipo == TipoToken.FALSE || 
        	preanalisis.tipo == TipoToken.NULL || 
        	preanalisis.tipo == TipoToken.NUMBER || 
        	preanalisis.tipo == TipoToken.STRING || 
        	preanalisis.tipo == TipoToken.IDENTIFIER || 
        	preanalisis.tipo == TipoToken.LEFT_PAREN){
            EXPRESSION();
        	match(TipoToken.SEMICOLON);
        }
        else if(preanalisis.tipo == TipoToken.SEMICOLON){
        	match(TipoToken.SEMICOLON);
        }
        else{
            hayErrores = true;
            System.out.println("Se esperaba '!' or '-' or 'true' or 'false' or 'null' or 'number' or 'string' or 'id' or '(' or ';' ");
        }
    }
    //FOR_STMT_3 -> EXPRESSION
    private void FOR_STMT_3(){
    	if(hayErrores)
            return;

        if(preanalisis.tipo == TipoToken.BANG || 
        	preanalisis.tipo == TipoToken.MINUS || 
        	preanalisis.tipo == TipoToken.TRUE || 
        	preanalisis.tipo == TipoToken.FALSE || 
        	preanalisis.tipo == TipoToken.NULL || 
        	preanalisis.tipo == TipoToken.NUMBER || 
        	preanalisis.tipo == TipoToken.STRING || 
        	preanalisis.tipo == TipoToken.IDENTIFIER || 
        	preanalisis.tipo == TipoToken.LEFT_PAREN){
            EXPRESSION();
        }
    }
    //IF_STMT -> if (EXPRESSION) STATEMENT ELSE_STATEMENT
    private void IF_STMT(){
    	if(hayErrores)
            return;

        if(preanalisis.tipo == TipoToken.IF){
            match(TipoToken.IF);
            match(TipoToken.LEFT_PAREN);
            EXPRESSION();
        	match(TipoToken.RIGHT_PAREN);
        	STATEMENT();
        	ELSE_STATEMENT();
        }
        else{
            hayErrores = true;
            System.out.println("Se esperaba 'if' ");
        }
    }
    //ELSE_STATEMENT -> else STATEMENT | /e
    private void ELSE_STATEMENT(){
    	if(hayErrores)
            return;

        if(preanalisis.tipo == TipoToken.ELSE){
            match(TipoToken.ELSE);
        	STATEMENT();
        }
    }
    //PRINT_STMT -> print EXPRESSION;
    private void PRINT_STMT(){
    	if(hayErrores)
            return;

        if(preanalisis.tipo == TipoToken.PRINT){
            match(TipoToken.PRINT);
            EXPRESSION();
        	match(TipoToken.SEMICOLON);
        }
        else{
            hayErrores = true;
            System.out.println("Se esperaba 'print' ");
        }
    }
    //RETURN_STMT -> return RETURN_STMT_OPC;
    private void RETURN_STMT(){
    	if(hayErrores)
            return;

        if(preanalisis.tipo == TipoToken.RETURN){
            match(TipoToken.RETURN);
            RETURN_STMT_OPC();
        	match(TipoToken.SEMICOLON);
        }
        else{
            hayErrores = true;
            System.out.println("Se esperaba 'return' ");
        }
    }
    //RETURN_STMT_OPC -> EXPRESSION | /e
    private void RETURN_STMT_OPC(){
    	if(hayErrores)
            return;

        if(preanalisis.tipo == TipoToken.BANG || 
        	preanalisis.tipo == TipoToken.MINUS || 
        	preanalisis.tipo == TipoToken.TRUE || 
        	preanalisis.tipo == TipoToken.FALSE || 
        	preanalisis.tipo == TipoToken.NULL || 
        	preanalisis.tipo == TipoToken.NUMBER || 
        	preanalisis.tipo == TipoToken.STRING || 
        	preanalisis.tipo == TipoToken.IDENTIFIER || 
        	preanalisis.tipo == TipoToken.LEFT_PAREN){
            EXPRESSION();
        }
    }
    //WHILE_STMT -> while (EXPRESSION) STATEMENT
    private void WHILE_STMT(){
    	if(hayErrores)
            return;

        if(preanalisis.tipo == TipoToken.WHILE){
            match(TipoToken.WHILE);
            match(TipoToken.LEFT_PAREN);
            EXPRESSION();
            match(TipoToken.RIGHT_PAREN);
            STATEMENT();
        }
        else{
            hayErrores = true;
            System.out.println("Se esperaba 'while' ");
        }
    }
    //BLOCK -> { DECLARATION }
    private void BLOCK(){
    	if(hayErrores)
            return;

        if(preanalisis.tipo == TipoToken.LEFT_BRACE){
            match(TipoToken.LEFT_BRACE);
            DECLARATION();
            match(TipoToken.RIGHT_BRACE);
        }
        else{
            hayErrores = true;
            System.out.println("Se esperaba '{' ");
        }
    }
    //EXPRESSION -> ASSIGNMENT
    private void EXPRESSION(){
    	if(hayErrores)
            return;

        if(preanalisis.tipo == TipoToken.BANG || 
        	preanalisis.tipo == TipoToken.MINUS || 
        	preanalisis.tipo == TipoToken.TRUE || 
        	preanalisis.tipo == TipoToken.FALSE || 
        	preanalisis.tipo == TipoToken.NULL || 
        	preanalisis.tipo == TipoToken.NUMBER || 
        	preanalisis.tipo == TipoToken.STRING || 
        	preanalisis.tipo == TipoToken.IDENTIFIER || 
        	preanalisis.tipo == TipoToken.LEFT_PAREN){
            ASSIGNMENT();
        }
        else{
            hayErrores = true;
            System.out.println("Se esperaba '!' or '-' or 'true' or 'false' or 'null' or 'number' or 'string' or 'id' or '(' ");
        }
    }
    //ASSIGNMENT -> LOGIC_OR ASSIGNMENT_OPC
    private void ASSIGNMENT(){
    	if(hayErrores)
            return;

        if(preanalisis.tipo == TipoToken.BANG || 
        	preanalisis.tipo == TipoToken.MINUS || 
        	preanalisis.tipo == TipoToken.TRUE || 
        	preanalisis.tipo == TipoToken.FALSE || 
        	preanalisis.tipo == TipoToken.NULL || 
        	preanalisis.tipo == TipoToken.NUMBER || 
        	preanalisis.tipo == TipoToken.STRING || 
        	preanalisis.tipo == TipoToken.IDENTIFIER || 
        	preanalisis.tipo == TipoToken.LEFT_PAREN){
            LOGIC_OR();
       	 	ASSIGNMENT_OPC();
        }
        else{
            hayErrores = true;
            System.out.println("Se esperaba '!' or '-' or 'true' or 'false' or 'null' or 'number' or 'string' or 'id' or '(' ");
        }
    }
    //ASSIGNMENT_OPC -> = EXPRESSION | /e
    private void ASSIGNMENT_OPC(){
    	if(hayErrores)
            return;

        if(preanalisis.tipo == TipoToken.EQUAL){
            match(TipoToken.EQUAL);
       	 	EXPRESSION();
        }
    }
    //LOGIC_OR -> LOGIC_AND LOGIC_OR_2
    private void LOGIC_OR(){
    	if(hayErrores)
            return;

        if(preanalisis.tipo == TipoToken.BANG || 
        	preanalisis.tipo == TipoToken.MINUS || 
        	preanalisis.tipo == TipoToken.TRUE || 
        	preanalisis.tipo == TipoToken.FALSE || 
        	preanalisis.tipo == TipoToken.NULL || 
        	preanalisis.tipo == TipoToken.NUMBER || 
        	preanalisis.tipo == TipoToken.STRING || 
        	preanalisis.tipo == TipoToken.IDENTIFIER || 
        	preanalisis.tipo == TipoToken.LEFT_PAREN){
            LOGIC_AND();
       	 	LOGIC_OR_2();
        }
        else{
            hayErrores = true;
            System.out.println("Se esperaba '!' or '-' or 'true' or 'false' or 'null' or 'number' or 'string' or 'id' or '(' ");
        }
    }
    //LOGIC_OR_2 -> or LOGIC_AND LOGIC_OR_2
    private void LOGIC_OR_2(){
    	if(hayErrores)
            return;

        if(preanalisis.tipo == TipoToken.OR){
        	match(TipoToken.OR);
            LOGIC_AND();
       	 	LOGIC_OR_2();
        }
    }
    //LOGIC_AND -> EQUALITY LOGIC_AND_2
    private void LOGIC_AND(){
    	if(hayErrores)
            return;

        if(preanalisis.tipo == TipoToken.BANG || 
        	preanalisis.tipo == TipoToken.MINUS || 
        	preanalisis.tipo == TipoToken.TRUE || 
        	preanalisis.tipo == TipoToken.FALSE || 
        	preanalisis.tipo == TipoToken.NULL || 
        	preanalisis.tipo == TipoToken.NUMBER || 
        	preanalisis.tipo == TipoToken.STRING || 
        	preanalisis.tipo == TipoToken.IDENTIFIER || 
        	preanalisis.tipo == TipoToken.LEFT_PAREN){
            EQUALITY();
       	 	LOGIC_AND_2();
        }
        else{
            hayErrores = true;
            System.out.println("Se esperaba '!' or '-' or 'true' or 'false' or 'null' or 'number' or 'string' or 'id' or '(' ");
        }
    }
    //LOGIC_AND_2 -> and EQUALITY LOGIC_AND_2 | /e
    private void LOGIC_AND_2(){
    	if(hayErrores)
            return;

        if(preanalisis.tipo == TipoToken.AND){
        	match(TipoToken.AND);
            EQUALITY();
       	 	LOGIC_AND_2();
        }
    }
    //EQUALITY -> COMPARISON EQUALITY_2
    private void EQUALITY(){
    	if(hayErrores)
            return;

        if(preanalisis.tipo == TipoToken.BANG || 
        	preanalisis.tipo == TipoToken.MINUS || 
        	preanalisis.tipo == TipoToken.TRUE || 
        	preanalisis.tipo == TipoToken.FALSE || 
        	preanalisis.tipo == TipoToken.NULL || 
        	preanalisis.tipo == TipoToken.NUMBER || 
        	preanalisis.tipo == TipoToken.STRING || 
        	preanalisis.tipo == TipoToken.IDENTIFIER || 
        	preanalisis.tipo == TipoToken.LEFT_PAREN){
            COMPARISON();
       	 	EQUALITY_2();
        }
        else{
            hayErrores = true;
            System.out.println("Se esperaba '!' or '-' or 'true' or 'false' or 'null' or 'number' or 'string' or 'id' or '(' ");
        }
    }
    //EQUALITY_2 -> != COMPARISON EQUALITY_2 | == COMPARISON EQUALITY_2 | /e
    private void EQUALITY_2(){
    	if(hayErrores)
            return;

        if(preanalisis.tipo == TipoToken.BANG_EQUAL){
        	match(TipoToken.BANG_EQUAL);
            COMPARISON();
       	 	EQUALITY_2();
        }
        else if(preanalisis.tipo == TipoToken.EQUAL_EQUAL){
        	match(TipoToken.EQUAL_EQUAL);
            COMPARISON();
       	 	EQUALITY_2();
        }
    }
    //COMPARISON -> TERM COMPARISON_2
    private void COMPARISON(){
    	if(hayErrores)
            return;

        if(preanalisis.tipo == TipoToken.BANG || 
        	preanalisis.tipo == TipoToken.MINUS || 
        	preanalisis.tipo == TipoToken.TRUE || 
        	preanalisis.tipo == TipoToken.FALSE || 
        	preanalisis.tipo == TipoToken.NULL || 
        	preanalisis.tipo == TipoToken.NUMBER || 
        	preanalisis.tipo == TipoToken.STRING || 
        	preanalisis.tipo == TipoToken.IDENTIFIER || 
        	preanalisis.tipo == TipoToken.LEFT_PAREN){
            TERM();
       	 	COMPARISON_2();
        }
        else{
            hayErrores = true;
            System.out.println("Se esperaba '!' or '-' or 'true' or 'false' or 'null' or 'number' or 'string' or 'id' or '(' ");
        }
    }
    //COMPARISON_2 -> > TERM COMPARISON_2 | >= TERM COMPARISON_2 | < TERM COMPARISON_2 | <= TERM COMPARISON_2
    private void COMPARISON_2(){
    	if(hayErrores)
            return;

        if(preanalisis.tipo == TipoToken.GREATER){
        	match(TipoToken.GREATER);
            TERM();
       	 	COMPARISON_2();
        }
        else if(preanalisis.tipo == TipoToken.GREATER_EQUAL){
        	match(TipoToken.GREATER_EQUAL);
            TERM();
       	 	COMPARISON_2();
        }
        else if(preanalisis.tipo == TipoToken.LESS){
        	match(TipoToken.LESS);
            TERM();
       	 	COMPARISON_2();
        }
        else if(preanalisis.tipo == TipoToken.LESS_EQUAL){
        	match(TipoToken.LESS_EQUAL);
            TERM();
       	 	COMPARISON_2();
        }
    }
    //TERM -> FACTOR TERM_2
    private void TERM(){
    	if(hayErrores)
            return;

        if(preanalisis.tipo == TipoToken.BANG || 
        	preanalisis.tipo == TipoToken.MINUS || 
        	preanalisis.tipo == TipoToken.TRUE || 
        	preanalisis.tipo == TipoToken.FALSE || 
        	preanalisis.tipo == TipoToken.NULL || 
        	preanalisis.tipo == TipoToken.NUMBER || 
        	preanalisis.tipo == TipoToken.STRING || 
        	preanalisis.tipo == TipoToken.IDENTIFIER || 
        	preanalisis.tipo == TipoToken.LEFT_PAREN){
            FACTOR();
       	 	TERM_2();
        }
        else{
            hayErrores = true;
            System.out.println("Se esperaba '!' or '-' or 'true' or 'false' or 'null' or 'number' or 'string' or 'id' or '(' ");
        }
    }
    //TERM_2 -> - FACTOR TERM_2 | + FACTOR TERM_2 | /e
    private void TERM_2(){
    	if(hayErrores)
            return;

        if(preanalisis.tipo == TipoToken.MINUS){
        	match(TipoToken.MINUS);
            FACTOR();
       	 	TERM_2();
        }
        else if(preanalisis.tipo == TipoToken.PLUS){
        	match(TipoToken.PLUS);
            FACTOR();
       	 	TERM_2();
        }

    }
    //FACTOR -> UNARY FACTOR_2
    private void FACTOR(){
    	if(hayErrores)
            return;

        if(preanalisis.tipo == TipoToken.BANG || 
        	preanalisis.tipo == TipoToken.MINUS || 
        	preanalisis.tipo == TipoToken.TRUE || 
        	preanalisis.tipo == TipoToken.FALSE || 
        	preanalisis.tipo == TipoToken.NULL || 
        	preanalisis.tipo == TipoToken.NUMBER || 
        	preanalisis.tipo == TipoToken.STRING || 
        	preanalisis.tipo == TipoToken.IDENTIFIER || 
        	preanalisis.tipo == TipoToken.LEFT_PAREN){
            UNARY();
       	 	FACTOR_2();
        }
        else{
            hayErrores = true;
            System.out.println("Se esperaba '!' or '-' or 'true' or 'false' or 'null' or 'number' or 'string' or 'id' or '(' ");
        }

    }
    //FACTOR_2 -> / UNARY FACTOR_2 | * UNARY FACTOR_2 | /e
    private void FACTOR_2(){
    	if(hayErrores)
            return;

        if(preanalisis.tipo == TipoToken.SLASH){
        	match(TipoToken.SLASH);
            UNARY();
       	 	FACTOR_2();
        }
        else if(preanalisis.tipo == TipoToken.STAR){
        	match(TipoToken.STAR);
            UNARY();
       	 	FACTOR_2();
        }
    }
    //UNARY -> ! UNARY | - UNARY | CALL
    private void UNARY(){
    	if(hayErrores)
            return;

        if(preanalisis.tipo == TipoToken.BANG){
        	match(TipoToken.BANG);
            UNARY();
        }
        else if(preanalisis.tipo == TipoToken.MINUS){
        	match(TipoToken.MINUS);
            UNARY();
        }
        else if(preanalisis.tipo == TipoToken.TRUE || 
        		preanalisis.tipo == TipoToken.FALSE || 
        		preanalisis.tipo == TipoToken.NULL || 
        		preanalisis.tipo == TipoToken.NUMBER || 
        		preanalisis.tipo == TipoToken.STRING || 
        		preanalisis.tipo == TipoToken.IDENTIFIER || 
        		preanalisis.tipo == TipoToken.LEFT_PAREN){
            CALL();
        }
        else{
            hayErrores = true;
            System.out.println("Se esperaba '!' or '-' or 'true' or 'false' or 'null' or 'number' or 'string' or 'id' or '(' ");
        }
    }
    //CALL -> PRIMARY CALL_2
    private void CALL(){
    	if(hayErrores)
            return;

        if(preanalisis.tipo == TipoToken.TRUE || 
        	preanalisis.tipo == TipoToken.FALSE || 
        	preanalisis.tipo == TipoToken.NULL || 
        	preanalisis.tipo == TipoToken.NUMBER || 
        	preanalisis.tipo == TipoToken.STRING || 
        	preanalisis.tipo == TipoToken.IDENTIFIER || 
        	preanalisis.tipo == TipoToken.LEFT_PAREN){
        	PRIMARY();
        	CALL_2();
        }
        else{
            hayErrores = true;
            System.out.println("Se esperaba 'true' or 'false' or 'null' or 'number' or 'string' or 'id' or '(' ");
        }
    }
    //CALL_2 -> ( ARGUMENTS_OPC ) CALL_2 | /e
    private void CALL_2(){
    	if(hayErrores)
            return;

        if(preanalisis.tipo == TipoToken.LEFT_PAREN){
        	match(TipoToken.LEFT_PAREN);
        	ARGUMENTS_OPC();
        	match(TipoToken.RIGHT_PAREN);
        	CALL_2();
        }
    }
    //PRIMARY -> true | false | null | number | string | id | (EXPRESSION)
    private void PRIMARY(){
    	if(hayErrores)
            return;

        if(preanalisis.tipo == TipoToken.TRUE ){
        	match(TipoToken.TRUE);
        }
        else if(preanalisis.tipo == TipoToken.FALSE ){
        	match(TipoToken.FALSE);
        }
        else if(preanalisis.tipo == TipoToken.NULL ){
        	match(TipoToken.NULL);
        }
        else if(preanalisis.tipo == TipoToken.NUMBER ){
        	match(TipoToken.NUMBER);
        }
        else if(preanalisis.tipo == TipoToken.STRING){
        	match(TipoToken.STRING);
        }
        else if(preanalisis.tipo == TipoToken.IDENTIFIER){
        	match(TipoToken.IDENTIFIER);
        }
        else if(preanalisis.tipo == TipoToken.LEFT_PAREN ){
        	match(TipoToken.LEFT_PAREN);
        	EXPRESSION();
        	match(TipoToken.RIGHT_PAREN);
        }
        else{
            hayErrores = true;
            System.out.println("Se esperaba'true' or 'false' or 'null' or 'number' or 'string' or 'id' or '(' ");
        }
    }
    //FUNCTION -> id ( PARAMETERS_OPC ) BLOCK
    private void FUNCTION(){
    	if(hayErrores)
            return;

        if(preanalisis.tipo == TipoToken.IDENTIFIER ){
        	match(TipoToken.IDENTIFIER);
        	match(TipoToken.LEFT_PAREN);
        	PARAMETERS_OPC();
        	match(TipoToken.RIGHT_PAREN);
        	BLOCK();
        }
        else{
            hayErrores = true;
            System.out.println("Se esperaba 'id' ");
        }
    }
    //FUNCTIONS -> FUN_DECL FUNCTIONS | /e
    private void FUNCTIONS(){
    	if(hayErrores)
            return;

        if(preanalisis.tipo == TipoToken.FUN ){
        	FUN_DECL();
        	FUNCTIONS();
        }
    }
    //PARAMETERS_OPC -> PARAMETERS | /e
    private void PARAMETERS_OPC(){
    	if(hayErrores)
            return;

        if(preanalisis.tipo == TipoToken.IDENTIFIER ){
        	PARAMETERS();
        }
    }
    //PARAMETERS -> id PARAMETERS_2
    private void PARAMETERS(){
    	if(hayErrores)
            return;

        if(preanalisis.tipo == TipoToken.IDENTIFIER ){
        	match(TipoToken.IDENTIFIER);
        	PARAMETERS_2();
        }
    }
    //PARAMETERS_2 -> , id PARAMETERS_2 | /e
    private void PARAMETERS_2(){
    	if(hayErrores)
            return;

        if(preanalisis.tipo == TipoToken.COMMA ){
        	match(TipoToken.COMMA);
        	match(TipoToken.IDENTIFIER);
        	PARAMETERS_2();
        }
    }
    //ARGUMENTS_OPC -> EXPRESSION ARGUMENTS | /e
    private void ARGUMENTS_OPC(){
    	if(hayErrores)
            return;

        if(preanalisis.tipo == TipoToken.TRUE || 
        	preanalisis.tipo == TipoToken.FALSE || 
        	preanalisis.tipo == TipoToken.NULL || 
        	preanalisis.tipo == TipoToken.NUMBER || 
        	preanalisis.tipo == TipoToken.STRING || 
        	preanalisis.tipo == TipoToken.IDENTIFIER || 
        	preanalisis.tipo == TipoToken.LEFT_PAREN){
        	EXPRESSION();
        	ARGUMENTS();
        }
    }
    //ARGUMENTS -> , EXPRESSION ARGUMENTS | /e
    private void ARGUMENTS(){
    	if(hayErrores)
            return;

        if(preanalisis.tipo == TipoToken.COMMA){
        	match(TipoToken.COMMA);
        	EXPRESSION();
        	ARGUMENTS();
        }
    }


    private void match(TipoToken tt){
        if(preanalisis.tipo == tt){
            i++;
            preanalisis = tokens.get(i);
        }
        else{
            hayErrores = true;
            System.out.println("Error encontrado");
            //System.out.println("Error encontrado en el token "+ preanalisis.tipo+ " "+tokens.get(i).posicion + " debio ser " + tt);
        }

    }

}
