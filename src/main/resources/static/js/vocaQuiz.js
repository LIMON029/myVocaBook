const header = $("meta[name='_csrf_header']").attr('content');
const token = $("meta[name='_csrf']").attr('content');
const answers = [$('#answer1'), $('#answer2'), $('#answer3'), $('#answer4')];

let quizType = -1;
let vocaList, categories;
let nowQuiz = 0;

const main = {
    init : function () {
        const _this = this;
        for(let i = 0; i < 4; i++){
            answers[i].on('click', function() {
                _this.checkAnswer(i);
            });
        }
        _this.getCategories();

        $('#startQuizBtn').on('click', function() {
            if(quizType == 1) {
                _this.getQuizItem(true);
                if(vocaList.length!=0) {
                    _this.setQuizItem();
                }
            } else if(quizType == 2) {
                _this.getQuizItem(false);
                if(vocaList.length!=0) {
                    _this.setQuizItem();
                }
            }
        });

        $('#getAllWordBtn').on('click', function() {
            quizType = 1;
        });

        $('#getWrongWordBtn').on('click', function() {
            quizType = 2;
        });

        $('#goNextBtn').on('click', function() {
            _this.setQuizItem(_this);
        });
    },
    getCategories : function() {
        const url = "/getCategories";
        $.ajax({
            method: 'POST',
            url: url,
            dataType: 'json',
            async: false,
            beforeSend: function(xhr){
                xhr.setRequestHeader(header, token);
            },
            success: function(data) {
                categories = data;
                const startTag = document.querySelector("#cat-start");
                const endTag = document.querySelector("#cat-end");
                $(startTag).append("<option value='0'>직접 추가</option>")
                $(endTag).append("<option value='0'>직접 추가</option>")
                for(let i = 1; i < categories.length + 1; i++){
                    $(startTag).append("<option value='"+ (i)+"'>" + (categories[i-1]) + "</option>")
                    $(endTag).append("<option value='"+ (i)+"'>" + (categories[i-1]) + "</option>")
                }
            },
            error: function(error) {
                alert(JSON.stringify(error));
            }
        });
    },
    getQuizItem : function (all) {
        const url = all ? "/getQuizItemAll" : "/getQuizItemWrong";
        const start = $('#cat-start').val();
        const end = $('#cat-end').val();
        const param = {
            start: start,
            end: end
        };
        $.ajax({
            method: 'POST',
            url: url,
            dataType: 'json',
            async: false,
            contentType:'application/json; charset=utf-8',
            data: JSON.stringify(param),
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
            },
            error: function(error) {
                alert(JSON.stringify(error));
            }
        });
    }
};

main.init();