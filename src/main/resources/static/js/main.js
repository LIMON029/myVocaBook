const main = {
    init : function () {
        const _this = this;
        $('#btn-search').on('click', function() {
            _this.search();
        });

        $('#en').keydown(function(event) {
            if(event.keyCode == 13) {
                _this.search();
            }
        });
    },
    search : function () {
        const en = $('#en').val();
        const url = '/searchWord/'+en;

        const header = $("meta[name='_csrf_header']").attr('content');
        const token = $("meta[name='_csrf']").attr('content');

        $.ajax({
            method: 'POST',
            url: url,
            async: false,
            dataType: 'text',
            beforeSend: function(xhr){
                xhr.setRequestHeader(header, token);
            },
            success: function(data) {
                $('#ko').val(data);
                window.location.href = "/main";
            },
            error: function(error) {
                alert(JSON.stringify(error));
            }
        });
    }
};

main.init();