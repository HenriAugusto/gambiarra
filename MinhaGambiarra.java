//Você reparou que cada tecla faz uma coisa.
//Na verdade todas essas coisas extendem uma classe (não uma interface) chamada Template.

//Porque todos esses desenhos funcionam com base em três métodos. Um é o draw(). Dentro do draw ele detecta se
// ta no começo do desenho ou no final. Se tiver no começo ele chama um método chamado fade_in(). 
//Se tiver no final um método chamado fade_out(). Aí no fade_out() ele detecta se o efeito de fade_out ja
//terminou. Se ja terminou ele altera uma booleana que faz com que o método draw() pare de chamar ele 
//próprio no frame seguinte.

// Mas geralmente cada tecla apertada desenha não uma coisa, mas várias coisas iguais. Pra isso eu usei
// nested classes. 

//Por exemplo: uma classe MuitasBolinhas. Ela controla o desenho de uma inner class 
//chamada UmaBolinha. Eu coloquei uma dentro da outra pra organizar melhor o código. Se não teria que ter
// um monte de variáveis globais que daria uma porra de conflito de nome. Melhor deixar todas as "UmaBolinha"
// juntas dentro de uma classe. E também pq essa classe MuitasBolinhas que vai controlar quantas bolinhas que serão 
//desenhadas. Pq dependendo da força com que o usuário aperta a tecla do teclado eu posso 
//desenhar 1, 3, 5, 7 ou 10 bolinhas menores. E dependendo da oitava, o tamanho das bolinhas vai ser 
//diferente.

//Eu quero que MuitasBolinhas use o meu Template, pra poder controlar os fades das bolinhas e quando elas
//devem ser desenhadas

//Aí o problema é, na minha classe Template, eu quero chamar o método update_position()
//de todas as bolinhas. Mas quando eu fiz o Template eu não sabia que eu ia ter uma classe chamada 
//MinhasBolinhas nem UmaBolinha!

//Aí eu fiz uma puta gambiarra.


class Template{
    String obj_name;
    float transparência = 0;
    int max_inner_objs = 100; //quantos objetos da nested class eu vou usar
    innerClass inner_objs[] = new innerClass[max_inner_objs]; //array dos objetos

    Template(String set_name){
        obj_name = set_name;

        for (int i=0; i<max_inner_objs; i++){
            inner_objs[i] = new innerClass(); //inicia os objetos e diz pra ele qual seu índice
        }

    }

    void draw(int quantas_tenho_que_desenhar) {
        
        if ( fading_in ){
            fade_in();
        }
    
        if ( fading_out ){
            fade_out();
        }

        //se for pra continuar desenhando essa coisa
        if ( draw_next_frame) { 
            Schedule(obj_name); //guarda uma mensagem que faz o método draw ser chamado no frame seguinte
        }

        //quantas_tenho_que_desenhar é o número coisas que vão ser desenhadas, dependendo da
        //força com que o usuário apertou a tecla
        for (int i=0; i<quantas_tenho_que_desenhar; i++){
            inner_objs[i].desenhar(); //chama a função do obj, essa que não tem nada
        }

    }
    
    void fade_in(){
        // calcula a transparência das coisas e etc
    }
    
    void fade_out(){
        // calcula a transparência das coisas e etc
        
        //se o fade out terminou, não quero continuar desenhando isso a cada frame
        if ( fade_out_is_done ){
            draw_next_frame = false;
        }

    }

    //faz parte da gambiarra. Vai ser extendida depois
    class innerClass {

        innerClass(){
        }

        void desenhar(){
            //propositadamente vazio
        }

    }
}

class MuitasBolinhas extends Template {
    UmaBolinha pequenas_bolinhas[] = new UmaBolinha();


    MuitasBolinha(){
        super("MuitasBolinha");
        for (int i=0; i<max_inner_objs; i++){
            //aqui que entra a gambiarra!
            //eu não vou usar o metódo clone() do objeto. Eu to de
            //fato dizendo que um igual o outro.
            inner_objs[i] = pequenas_bolinhas[i];
            //assim quando a função draw() do Template chamar
            // inner_objs[i].draw() ela vai na verdade chamar
            // o método pequenas_bolinhas[i].draw()
            // ou seja, vai chamar o método .draw das bolinhas!
        }
    }

    void draw(){
        //pra controlar os fades e saber se é pra continuar desenhando.
        //E também pra chamar o método draw() de cada bolinha.
        super.draw();
    }

    //cada bolinha
    class UmaBolinha extends innerClass{
        PVector posição = new PVector(500, 500); //vector que guarda a posição da bolinha

        UmaBolinha(){
        }
        
        void draw(){
            //o método draw() da innerClass do Template tava vazio.
            //Pq eu ja sei que ele vai ser shadowed por esse draw aqui.
            //Aí esse é o código onde de fato eu desenho a bolinha na tela
            desenhar(círculo, posição, transparência);
        }

    }

}




//aqui vem a função draw, que é chamada a cada frame do programa
MuitasBolinhas minhas_bolinhas = new MuitasBolinhas();

void draw(){
    if (usuário_apertou_tecla){
        int força = tecla.getForce();
        minhas_bolinhas.draw( força ); //o parâmetro força é usado pra saber quantas bolinhas desenhar
    }
}

//função que roda antes do primeiro frame
void setup(){
    size(500,500);//tamanho da tela
}
