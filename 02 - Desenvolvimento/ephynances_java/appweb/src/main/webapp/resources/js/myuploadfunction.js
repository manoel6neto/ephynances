jQuery(function () {
 
    jQuery('#fileupload').fileupload({
 
        dataType: 'json',
 
        done: function (e, data) {
            jQuery("tr:has(td)").remove();
            $.each(data.result, function (index, file) {
 
                jQuery("#uploaded-files").append(
                    jQuery('<tr/>')
                    .append(jQuery('<td/>').text(file.fileName))
                    .append(jQuery('<td/>').text(file.fileSize))
                    .append(jQuery('<td/>').text(file.fileType))
                    .append(jQuery('<td/>').html("<a href='upload?f="+index+"'>Click</a>"))
                    .append(jQuery('<td/>').text("@"+file.twitter))
 
                    )//end jQuery("#uploaded-files").append()
            }); 
        },
 
        progressall: function (e, data) {
            var progress = parseInt(data.loaded / data.total * 100, 10);
            jQuery('#progress .bar').css(
                'width',
                progress + '%'
                );
        },
 
        dropZone: jQuery('#dropzone')
    }).bind('fileuploadsubmit', function (e, data) {
        // The example input, doesn't have to be part of the upload form:
        var twitter = jQuery('#twitter');
        data.formData = {
            twitter: twitter.val()
            };        
    });
 
});