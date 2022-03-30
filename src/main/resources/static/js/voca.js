const header = $("meta[name='_csrf_header']").attr('content');
const token = $("meta[name='_csrf']").attr('content');

function deleteVoca(id) {
    const url = "/deleteWord/"+id;

    $.ajax({
        method: 'POST',
        url: url,
        dataType: 'text',
        beforeSend: function(xhr){
            xhr.setRequestHeader(header, token);
        },
        success: function(data) {
            console.log(data);
            if(data=="삭제") {
                alert(id + "번째 단어가 삭제되었습니다.");
                window.location.href = "/myVoca";
            }
        },
        error: function(error) {
            alert(JSON.stringify(error));
        }
    });
};

const main = {
    init : function () {
        const _this = this;
        $('#modalSaveBtn').on('click', function() {
            _this.saveVoca();
        });
    },
    saveVoca : function () {
        const url = '/saveWord';

        const en = $('#en-content').val();
        const ko = $('#ko-content').val();

        const param = {
            en: en,
            ko: ko
        };
        if(en == "" || ko == ""){
            alert("단어를 입력해주세요");
            return
        }
        $.ajax({
            method: 'POST',
            url: url,
            dataType: 'text',
            contentType:'application/json; charset=utf-8',
            data: JSON.stringify(param),
            beforeSend: function(xhr){
                xhr.setRequestHeader(header, token);
            },
            success: function(data) {
            console.log(data);
                if(data=="저장") {
                    window.location.href = "/myVoca";
                }
            },
            error: function(error) {
                alert(JSON.stringify(error));
            }
        });
    }
};

main.init();