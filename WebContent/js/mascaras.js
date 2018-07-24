

function validaCamposInteirosNaoNulos(elemento) {
	if (document.getElementById(elemento.id).value == "") {
		document.getElementById(elemento.id).value = "0";
	}
}

function mascara(o, f) {
	v_obj = o
	v_fun = f
	setTimeout("execmascara()", 1)
}

function execmascara() {
	v_obj.value = v_fun(v_obj.value)
}

function soNumeros(v) {
	v = v.replace(/\D/g, "");
	v = v.replace( /[~^'`]/g, "" );
	return v;
}

function soNumerosMenorDez(v){
    v=v.replace(/\D/g, "")               //Remove tudo o que não é dígito
    v=v.replace(/^(\d{1})(\d)/, "$1.$2") //Coloca um ponto entre o primeiro 
    									//e o segundo dígitos
    return v;
}


function telefone(v) {
	v = soNumeros(v); 						// Remove tudo o que não é dígito
	v = v.replace(/(\d{4})(\d)/, "$1-$2");	// Coloca hífen entre o quarto e o quinto dígitos	
	return v
}

function hora(v) {
	v = soNumeros(v); 						// Remove tudo o que não é dígito
	v = v.replace(/(\d{2})(\d)/, "$1:$2");	// Coloca hífen entre o quarto e o quinto dígitos	
	return v
}

function cpf(v) {
	v = v.replace(/\D/g, "") // Remove tudo o que não é dígito
	v = v.replace(/(\d{3})(\d)/, "$1.$2") // Coloca um ponto entre o terceiro
											// e o quarto dígitos
	v = v.replace(/(\d{3})(\d)/, "$1.$2") // Coloca um ponto entre o terceiro
											// e o quarto dígitos
	// de novo (para o segundo bloco de números)
	v = v.replace(/(\d{3})(\d{1,2})$/, "$1-$2") // Coloca um hífen entre o
												// terceiro e o quarto dígitos
	return v
}
function data(v) {
	v = v.replace(/\D/g, "") // Remove tudo o que não é dígito
	v = v.replace(/^(\d{2})(\d)/, "$1/$2") // Coloca barra entre o segundo e o
											// terceiro dígitos
	v = v.replace(/^(\d{2})\/(\d{2})(\d)/, "$1/$2/$3") // Coloca barra entre o
														// quarto e o quinto
														// dígitos
	return v
}
function cep(v) {
	v = v.replace(/\D/g, "") // Remove tudo o que não é dígito
	v = v.replace(/^(\d{5})(\d)/, "$1-$2") // Esse é tão fácil que não merece
											// explicações
	return v
}

function cnpj(v) {
	v = v.replace(/\D/g, "") // Remove tudo o que não é dígito
	v = v.replace(/^(\d{2})(\d)/, "$1.$2") // Coloca ponto entre o segundo e o
											// terceiro dígitos
	v = v.replace(/^(\d{2})\.(\d{3})(\d)/, "$1.$2.$3") // Coloca ponto entre o
														// quinto e o sexto
														// dígitos
	v = v.replace(/\.(\d{3})(\d)/, ".$1/$2") // Coloca uma barra entre o
												// oitavo e o nono dígitos
	v = v.replace(/(\d{4})(\d)/, "$1-$2") // Coloca um hífen depois do bloco
											// de quatro dígitos
	return v
}



function soNumerosReais(v) {
        var x
	v = v.replace(/[^\d,\.]/g, "")
	v = v.replace(",", ".")
        v = v.replace("..", ".")
	if (v.length == 10)
		v = v.substring(0, 9)
        for(var i =0; i< v.length; i++){
           if(v[i] == "."){
               v = v.substring(0,i+1)+ v.substring(i+1, v.length).replace(".", "")
           }
        }
	return v
}

function soNumerosReaisAte24(v) {
	v = v.replace(/[^\d,\.]/g, "")
	v = v.replace(",", ".")
        
	if (v.length == 1) {
		if (v[0] == ".")
			v = "0."
	}
	if (v.length == 6)  
            v = v.substring(0, 5)
        if((v > 2)&&(v.length == 1))
            v = v.replace(v,"")
        if(v.length == 2 && v[1] > 4 && v[0] == 2)
            v = v.replace(v[1],"")
        if(v.length == 3 && v[0] != 0 && v[2] > 0)
            v[2] = v[2].replace(v[2],"")
        if(v > 24)
            v = v[0]+v[1]
        for(var i =0; i< v.length; i++){
           if(v[i] == "."){
               v = v.substring(0,i+1)+ v.substring(i+1, v.length).replace(".", "")
           }
        }
	return v
}

/*Função que padroniza valor monetário*/
function valor(v) {
	v = v.replace(/\D/g, "") // Remove tudo o que não é dígito
	v = v.replace(/^([0-9]{3}\.?){3}-[0-9]{2}$/, "$1.$2");
	v = v.replace(/(\d)(\d{2})$/, "$1.$2") // Coloca ponto antes dos 2 últimos
											// digitos
	return v
}

function SubstituiVirgulaPorPonto(campo) {
        campo = campo.replace(/[^\d,\.]/g, "")
	campo = campo.replace(/,/gi, ".");
        return campo;
}

function soNumerosAfter(v) {
	if (v == "") {
		v = 0;
	}
	return v
}

function soLetras(v) {
	v = v.replace(/\d/g, "")
	return v
}

function soLetrasDeVerdade(v) {
	v = v.replace(/[-!"#$%&'()*+,.\/:;<=>?@[\\\]_`{|}~°º·ª§¬¢£³²¹0-9]/g, "")
	return v
}


function semEspecial(v) {
	v = v.replace(/[#$%&()*+\/:;<=>@[\\\]_`{|}~°º·ª§¬¢£³²¹]/g, "")
	return v
}

/* Exemplo para permitir numeros romanos*/
function romanos(v) {
	v = v.toUpperCase() // Maiúsculas
	v = v.replace(/[^IVXLCDM]/g, "") // Remove tudo o que não for I, V, X, L,
										// C, D ou M
	// Essa é complicada! Copiei daqui:
	// http://www.diveintopython.org/refactoring/refactoring.html
	while (v.replace(
			/^M{0,4}(CM|CD|D?C{0,3})(XC|XL|L?X{0,3})(IX|IV|V?I{0,3})$/, "") != "")
		v = v.replace(/.$/, "")
	return v
}
function leech(v) {
	v = v.replace(/o/gi, "0") // coloca 0 no lugar de o
	v = v.replace(/i/gi, "1") // coloca 1 no lugar de i
	v = v.replace(/z/gi, "2") // coloca 2 no lugar de z
	v = v.replace(/e/gi, "3") // coloca 3 no lugar de e
	v = v.replace(/a/gi, "4") // coloca 4 no lugar de a
	v = v.replace(/s/gi, "5") // coloca 5 no lugar de s
	v = v.replace(/t/gi, "7") // coloca 7 no lugar de t
	return v
}
function horario(v) {
	v = v.replace(/\D/g, "")// Remove tudo o que não é dígito
	v = v.replace(/(\d{2})(\d)/, "$1:$2") // Coloca : entre o segundo e o
											// terceiro dígito
	if (v.length == 6)
		v = v.substring(0, 5)
	return v
}

var ancho = 300;
function progreso_tecla(obj, max) {
	if (obj.value.length < max) {
		var pos = ancho - parseInt((ancho * parseInt(obj.value.length)) / 200);
	} else {
		obj.value = obj.value.substring(0, max);
	}
}

function peso(v) {
	v = v.replace(/[^\d,\.]/g, "");
	v = v.replace(",", ".");
	//remove letras e substitui ',' por '.'

	if (v.charAt(0) == "0") {
		v = v.replace(/(\d{1})(\d)/, "$1.$2");
		v = v.substring(0, 3);
	} else {
		if (v.charAt(0) == ".") {
			v = "0" + v;
		}
		if (v.charAt(1) == ".") {
			v = v.replace(/(\d{1})(\d)/, "$1.$2");
			v = v.substring(0, 3);
		}
		else {
			if (v.charAt(1) == "0") {
			//vai ser 10
			if (v.charAt(0) != "1"){
				v = "10";
			}
			if (v.charAt(1) == "0") { //digitou 10
					v = v.replace(/(\d{2})(\d)/, "$1.$2");
					v += ".0";
					v = v.substring(0, 4);
			}
				
			} else {
				v = v.replace(/(\d{1})(\d)/, "$1.$2");
				v = v.substring(0, 3);
			}
		}
	}
	if (v.charAt(2) == ".") {
				v = v.substring(0, 2);
	}
	return v;
	
	}
/**
 * 
 * Função para remover letras e manter somente números.
 * mascara no modelo para valores de 00:00 23:59
 * 
 * @param v
 * @returns
 */
function hora(v){
    v=v.replace(/\D/g,"")
    v=v.replace(/^(\d{2})(\d)/,"$1:$2")
    if(v.length == 1){
        if(v.match(/[3-9]/))
        v=""
    }else if(v.length == 2){
        if( v.match(/[2][4-9]/))
        v=v.replace(/^(\d{1})(\d)/,"$1")
    }else if( v.length == 4){
        if( v.match(/[0-9]*:[6-9]/))
        v=v.substring(0, 2)
    }                            
    return v
}

	/**
	 * 
	 * Função para remover letras e manter somente números.
	 * mascara no modelo 9999999-9 (dígito verificador).
	 * 
	 * @param v
	 * @returns
	 */
	function digitoVerificador(v) {

		v = v.replace(/\D/g, "");
		v = v.replace(/(\d)(\d{1})$/, "$1-$2");
		
		return v;
	}

	
function soLetrasNumeros(v){
v = v.replace(/[^0-9A-Za-z]/g, "");
return v;
}
	
function nota(o,t) {
	v_obj = o;
	v_fun = t;
	setTimeout("notaimple()", 1);	
	}
//nomes com espaço
$(".nomePessoa").alpha({
allow:" "
});


function obrigatorioOcorrenciaIncluirAlterar() {
    if (document.getElementById('lista:tipoOcorrencia:1').checked) {
        document.getElementById('lista:obrigatorioAcaoDisciplinar').style.display = 'none';
    } else if (document.getElementById('lista:tipoOcorrencia:0').checked) {
        document.getElementById('lista:obrigatorioAcaoDisciplinar').style.display = '';
    }
}