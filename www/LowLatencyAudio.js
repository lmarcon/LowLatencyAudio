

var exec = require('cordova/exec');

var LowLatencyAudio = {

	preloadFX: function ( id, assetPath, success, fail) {
		return exec(success || this._emptyCallback, fail || this._emptyCallback, "LowLatencyAudio", "preloadFX", [id, assetPath]);
	},    

	preloadAudio: function ( id, assetPath, voices, success, fail) {
		return exec(success || this._emptyCallback, fail || this._emptyCallback, "LowLatencyAudio", "preloadAudio", [id, assetPath, voices]);
	},

	play: function (id, success, fail) {
		return exec(success || this._emptyCallback, fail || this._emptyCallback, "LowLatencyAudio", "play", [id]);
	},

	stop: function (id, success, fail) {
		return exec(success || this._emptyCallback, fail || this._emptyCallback, "LowLatencyAudio", "stop", [id]);
	},

	loop: function (id, success, fail) {
		return exec(success || this._emptyCallback, fail || this._emptyCallback, "LowLatencyAudio", "loop", [id]);
	},

	unload: function (id, success, fail) {
		return exec(success || this._emptyCallback, fail || this._emptyCallback, "LowLatencyAudio", "unload", [id]);
	},

	setVolume: function(id, volume, success, fail) {
		return exec(success || this._emptyCallback, fail || this._emptyCallback, "LowLatencyAudio", "setVolume", [id, volume]);    
	},

	pause: function(id, success, fail) {
		return exec(success || this._emptyCallback, fail || this._emptyCallback, "LowLatencyAudio", "pause", [id]);        
	},

	_emptyCallback: function(){}

};

module.exports = LowLatencyAudio;