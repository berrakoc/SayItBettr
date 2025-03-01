from flask import Flask, request, jsonify
import librosa
import numpy as np
import tensorflow as tf
from tensorflow.keras.models import Model
from tensorflow.keras.layers import Input, Dense, Lambda

app = Flask(__name__)

# 1. MFCC Özelliklerini Çıkaran Fonksiyon
def extract_mfcc(file_path):
    y, sr = librosa.load(file_path, sr=None)
    mfcc = librosa.feature.mfcc(y=y, sr=sr, n_mfcc=40)
    return np.mean(mfcc.T, axis=0)

# 2. Temel Ağ Modeli
def create_base_network(input_shape):
    input_layer = Input(shape=input_shape)
    x = Dense(128, activation='relu')(input_layer)
    x = Dense(128, activation='relu')(x)
    x = Dense(128, activation='relu')(x)
    return Model(input_layer, x)

# 3. Siamese Neural Network Modeli
def create_siamese_network(input_shape):
    base_network = create_base_network(input_shape)

    input_a = Input(shape=input_shape)
    input_b = Input(shape=input_shape)

    processed_a = base_network(input_a)
    processed_b = base_network(input_b)

    distance = Lambda(lambda x: tf.sqrt(tf.reduce_sum(tf.square(x[0] - x[1]), axis=-1)))([processed_a, processed_b])

    model = Model([input_a, input_b], distance)
    return model

# 4. Modeli Oluştur ve Derle
input_shape = (40,)
siamese_network = create_siamese_network(input_shape)
siamese_network.compile(loss='mse', optimizer='adam', metrics=['accuracy'])

# 5. Ses Karşılaştırma Fonksiyonu
def compare_voices(file1, file2):
    ref_features = extract_mfcc(file1)
    user_features = extract_mfcc(file2)

    ref_features = np.expand_dims(ref_features, axis=0)
    user_features = np.expand_dims(user_features, axis=0)

    distance = siamese_network.predict([ref_features, user_features])[0]
    max_possible_distance = np.sqrt(np.sum(np.square(ref_features - np.mean(ref_features))))
    similarity_percentage = max(0, 100 - (distance / max_possible_distance) * 100)

    return similarity_percentage

# 6. Flask API Endpoint
@app.route('/compare', methods=['POST'])
def compare_audio():
    if 'file1' not in request.files or 'file2' not in request.files:
        return jsonify({'error': 'Lütfen iki ses dosyası yükleyin!'}), 400

    file1 = request.files['file1']
    file2 = request.files['file2']

    file1_path = "temp1.wav"
    file2_path = "temp2.wav"

    file1.save(file1_path)
    file2.save(file2_path)

    similarity = compare_voices(file1_path, file2_path)

    return jsonify({'similarity': f'{similarity:.2f}%'})

if __name__ == '__main__':
    app.run(host='0.0.0.0', port=5000, debug=True)
