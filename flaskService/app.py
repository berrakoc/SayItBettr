from flask import Flask, request, jsonify
import librosa
import numpy as np
import tensorflow as tf
from tensorflow.keras.models import Model
from tensorflow.keras.layers import Input, Dense, Lambda
from pydub import AudioSegment

app = Flask(__name__)

# ffmpeg yolunu ayarla
AudioSegment.converter = "C:/ffmpeg/bin/ffmpeg.exe"
AudioSegment.ffprobe = "C:/ffmpeg/bin/ffprobe.exe"

def convert_to_wav(input_path, output_path):
    """MP3 veya diğer formatları WAV'e dönüştürür."""
    audio = AudioSegment.from_file(input_path)
    audio.export(output_path, format="wav")

def extract_mfcc(file_path):
    """Ses dosyasının MFCC özelliklerini çıkarır."""
    if not file_path.endswith('.wav'):
        wav_path = file_path.rsplit('.', 1)[0] + '.wav'
        convert_to_wav(file_path, wav_path)
        file_path = wav_path

    y, sr = librosa.load(file_path, sr=None)
    mfcc = librosa.feature.mfcc(y=y, sr=sr, n_mfcc=40)
    return np.mean(mfcc.T, axis=0)

def create_base_network(input_shape):
    """Temel ağ modelini oluşturur."""
    input_layer = Input(shape=input_shape)
    x = Dense(128, activation='relu')(input_layer)
    x = Dense(128, activation='relu')(x)
    x = Dense(128, activation='relu')(x)
    return Model(input_layer, x)

def create_siamese_network(input_shape):
    """Siamese Neural Network modelini oluşturur."""
    base_network = create_base_network(input_shape)

    input_a = Input(shape=input_shape)
    input_b = Input(shape=input_shape)

    processed_a = base_network(input_a)
    processed_b = base_network(input_b)

    distance = Lambda(lambda x: tf.sqrt(tf.reduce_sum(tf.square(x[0] - x[1]), axis=-1)))([processed_a, processed_b])

    return Model([input_a, input_b], distance)

app = Flask(__name__)

input_shape = (40,)
siamese_network = create_siamese_network(input_shape)
siamese_network.compile(loss='mse', optimizer='adam', metrics=['accuracy'])

def compare_voices(file1, file2):
    """İki ses dosyasını karşılaştırır ve benzerlik yüzdesini hesaplar."""
    ref_features = extract_mfcc(file1)
    user_features = extract_mfcc(file2)

    ref_features = np.expand_dims(ref_features, axis=0)
    user_features = np.expand_dims(user_features, axis=0)

    distance = siamese_network.predict([ref_features, user_features])[0]
    max_possible_distance = np.sqrt(np.sum(np.square(ref_features - np.mean(ref_features))))
    similarity_percentage = max(0, 100 - (distance / max_possible_distance) * 100)

    return similarity_percentage

@app.route('/compare', methods=['POST'])
def compare_audio():
    """Flask API endpoint: İki ses dosyasını karşılaştırır."""
    if 'file1' not in request.files or 'file2' not in request.files:
        return jsonify({'error': 'Lütfen iki ses dosyası yükleyin!'}), 400

    file1 = request.files['file1']
    file2 = request.files['file2']

    file1_extension = file1.filename.rsplit('.', 1)[-1].lower()
    file2_extension = file2.filename.rsplit('.', 1)[-1].lower()

    file1_path = f"temp1.{file1_extension}"
    file2_path = f"temp2.{file2_extension}"

    file1.save(file1_path)
    file2.save(file2_path)

    # Eğer dosya MP3 formatındaysa, önce WAV formatına çevir
    if file1_extension != 'wav':
        wav_file1_path = "temp1.wav"
        convert_to_wav(file1_path, wav_file1_path)
        file1_path = wav_file1_path  # Yeni yolu güncelle

    if file2_extension != 'wav':
        wav_file2_path = "temp2.wav"
        convert_to_wav(file2_path, wav_file2_path)
        file2_path = wav_file2_path  # Yeni yolu güncelle


    similarity = compare_voices(file1_path, file2_path)

    return jsonify({'similarity': f'{similarity:.2f}%'})

if __name__ == '__main__':
    app.run(host='0.0.0.0', port=5000, debug=True)

