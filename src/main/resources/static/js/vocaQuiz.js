const header = $("meta[name='_csrf_header']").attr('content');
const token = $("meta[name='_csrf']").attr('content');
const answers = [$('#answer1'), $('#answer2'), $('#answer3'), $('#answer4')];
let vocaList
let nowQuiz = 0
const main = {
    init : function () {
        const _this = this;
        for(let i = 0; i < 4; i++){
            answers[i].on('click', function() {
                _this.checkAnswer(i);
            });
        }

        $('#getAllWordBtn').on('click', function() {
            _this.getQuizItem(true);
            if(vocaList.length!=0) {
                _this.setQuizItem();
            }
        });

        $('#getWrongWordBtn').on('click', function() {
            _this.getQuizItem(false);
            if(vocaList.length!=0) {
                _this.setQuizItem();
            }
        });

        $('#goNextBtn').on('click', function() {
            _this.setQuizItem(_this);
        });
    },
    getQuizItem : function (all) {
        const url = all ? "/getQuizItemAll" : "/getQuizItemWrong";
        $.ajax({
            method: 'POST',
            url: url,
            dataType: 'json',
            async: false,
            beforeSend: function(xhr){
                xhr.setRequestHeader(header, token);
            },
            success: function(data) {
                vocaList = data;
                console.log(JSON.stringify(data));
                if(vocaList.length==0){
                    alert("저장된 단어가 없습니다.");
                }else {
                    $('#answerWrapper').removeClass("hidden");
                    $('#indexWrapper').addClass("hidden");
                }
            },
            error: function(error) {
                alert(JSON.stringify(error));
            }
        });
    },
    setQuizItem : function (_this) {
        const en = vocaList[nowQuiz].en;
        const url = "/getQuizItem/" + en;
        $.ajax({
            method: 'POST',
            url: url,
            dataType: 'json',
            async: false,
            beforeSend: function(xhr){
                xhr.setRequestHeader(header, token);
            },
            success: function(data) {
                $('#quiz').text(en);
                for(let i = 0; i < 4; i++){
                    answers[i].attr("disabled", false);
                    answers[i].text(data[i]);
                    answers[i].removeClass("correctAnswer");
                    answers[i].removeClass("wrongAnswer");
                    answers[i].removeClass("anotherAnswer");
                }
                $('#goNextBtn').addClass("hidden");
                console.log(JSON.stringify(data));
            },
            error: function(error) {
                alert(JSON.stringify(error));
            }
        });
    },
    checkAnswer : function(answer) {
        const en = vocaList[nowQuiz].en;
        const url = "/checkAnswer/" + answer;
        $.ajax({
            method: 'POST',
            url: url,
            dataType: 'json',
            async: false,
            beforeSend: function(xhr){
                xhr.setRequestHeader(header, token);
            },
            success: function(data) {
                $('#quiz').text(en);
                const values = data;
                answers[data.answer].addClass("correctAnswer");
                if(data.wrong != data.answer){
                    answers[data.wrong].addClass("wrongAnswer");
                }
                for(let i = 0; i < 4; i++) {
                    answers[i].attr("disabled", true);
                    if(!answers[i].hasClass("correctAnswer")&&!answers[i].hasClass("wrongAnswer")){
                        answers[i].addClass("anotherAnswer");
                    }
                }

                nowQuiz += 1;
                $('#goNextBtn').removeClass("hidden");
                if(nowQuiz == vocaList.length){
                    $('#goNextBtn').addClass("hidden");
                    $('#submitAnswers').removeClass("hidden");
                }
                console.log(JSON.stringify(data));
            },
            error: function(error) {
                alert(JSON.stringify(error));
            }
        });
    }
};

main.init();