var x;
var igual;
var jaClicou;
var habilita = true;
function pegaNome(texto, checkBox) {
    habilita = false;
    if (texto === 'Relatórios') {
        if (checkBox.checked) {
            checkBox.parentNode.parentNode.parentNode.parentNode.parentNode.parentNode.parentNode.getElementsByTagName('li')[1].className = 'ui-state-default ui-corner-top';
        } else {
            checkBox.parentNode.parentNode.parentNode.parentNode.parentNode.parentNode.parentNode.getElementsByTagName('li')[1].className = 'ui-state-default ui-corner-top null ui-state-disabled'//style.display = 'none';
        }
    }
}
function comentada(){
    this.x = x;
    var s = x.toString().substr(x.toString().lastIndexOf("_") - 1, 1);
    if (s * 1 === 9) {
        s = x.toString().substr(x.toString().lastIndexOf("_") - 2, 1);
        s = (s * 1) + 1;
        s = s + '0';
    } else {
        s = (s * 1) + 1;
    }
    var k = x.toString().substring(0, x.toString().lastIndexOf("_") - s.toString().length) + s;
    if (k !== igual.name) {
        var filhos = document.getElementById(x).childNodes;
        for (var i = 0; filhos.length > i; i++) {
            var filho = filhos[i];
            if (filho.name === k) {
                filho.checked = true;
                filho.disable = true;
                break;
            }
        }
    }
}
function setX(x, igual, paiZao) {
    if (igual.checked) {
        document.getElementById(x).getElementsByTagName("input")[0].checked = true;
        paiZao.getElementsByTagName('input')[1].checked = true;
    } else {
        checkBoxes = igual.parentNode.parentNode.getElementsByTagName("input");
        if (checkBoxes[0] === igual) {
            for (var i = 0; checkBoxes.length > i; i++) {
                checkBoxes[i].checked = false;
            }
        }
    }
}
function getX() {
    return this.x;
}

function setCheckedOrNot(checkBoxes) {
    entroElse = false;
    habilita = true;
    for (var i = 0; checkBoxes.length > i; i++) {
        if (checkBoxes[i].checked){
            habilita = false;
            break;
        }
    }
    for (var i = 0; checkBoxes.length > i; i++) {
        if (habilita) {
            checkBoxes[i].checked = true;
        } else {
            checkBoxes[i].checked = false;
            entroElse = true;
        }
    }
    if (entroElse) {
        habilita = true;
    } else {
        habilita = false;
    }
}

function setarRelatorio(verificador) {
    if (verificador) {
        document.getElementsByTagName('li')[37].className = 'ui-state-default ui-corner-top'; //fixme arrumar javaScript
    } else {
        document.getElementsByTagName('li')[37].className = 'ui-state-default ui-corner-top null ui-state-disabled';
    }
}
//faltando colocar OK para o relatorio
function selecionarTodos(element) {
    checkBoxes = document.getElementsByTagName("input");
    setCheckedOrNot(checkBoxes);
    setarRelatorio(checkBoxes[0].checked);
    //document.getElementsByTagName('li')[37].className = 'ui-state-default ui-corner-top';
    //document.getElementsByTagName('li')[37].className = 'ui-state-default ui-corner-top null ui-state-disabled'

}


function selecionarTodosPorCampo(element, nome) {
    checkBoxes = element.parentNode.parentNode.getElementsByTagName("input");
    setCheckedOrNot(checkBoxes);
    if (nome === 'Controle de Manutenção') {
        setarRelatorio(checkBoxes[0].checked)
    }

}

function desmarcarTodosPai(element) {
    habilita = true;
    if (!element.checked) {
        checkBoxes = element.parentNode.parentNode.parentNode.getElementsByTagName("input");
        for (var i = 0; checkBoxes.length > i; i++) {
            checkBoxes[i].checked = false;
        }
    }
}