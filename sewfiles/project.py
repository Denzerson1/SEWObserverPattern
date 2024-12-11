import cv2
import mediapipe as mp
import numpy as np

# Initialize Mediapipe Face Detection and Emotion Recognition Tools
mp_face_detection = mp.solutions.face_detection
mp_drawing = mp.solutions.drawing_utils

# Load emotion labels (you can use a more comprehensive model or dataset)
emotion_labels = ["Neutral", "Happy", "Sad", "Surprised", "Angry"]

# Function to classify emotions (you can replace this with a trained model)
def classify_emotion(face):
    # Dummy implementation for demo (replace with real classification logic)
    avg_color = np.mean(face)
    if avg_color > 150:
        return "Happy"
    elif avg_color > 100:
        return "Neutral"
    else:
        return "Sad"

# Start Video Capture
cap = cv2.VideoCapture(0)

with mp_face_detection.FaceDetection(min_detection_confidence=0.5) as face_detection:
    while cap.isOpened():
        ret, frame = cap.read()
        if not ret:
            print("Failed to capture frame. Exiting...")
            break
        
        # Convert frame to RGB
        rgb_frame = cv2.cvtColor(frame, cv2.COLOR_BGR2RGB)
        results = face_detection.process(rgb_frame)
        
        # Draw face detections and classify emotions
        if results.detections:
            for detection in results.detections:
                # Get bounding box
                bboxC = detection.location_data.relative_bounding_box
                ih, iw, _ = frame.shape
                x, y, w, h = int(bboxC.xmin * iw), int(bboxC.ymin * ih), \
                             int(bboxC.width * iw), int(bboxC.height * ih)
                
                # Extract face region for emotion classification
                face = frame[y:y+h, x:x+w]
                emotion = classify_emotion(face)
                
                # Draw bounding box and label
                cv2.rectangle(frame, (x, y), (x+w, y+h), (255, 0, 0), 2)
                cv2.putText(frame, emotion, (x, y-10), cv2.FONT_HERSHEY_SIMPLEX, 
                            1, (255, 255, 255), 2, cv2.LINE_AA)
        
        # Display the output
        cv2.imshow("Facial Expression Recognition", frame)
        
        # Break on 'q' key press
        if cv2.waitKey(5) & 0xFF == ord('q'):
            break

# Release resources
cap.release()
cv2.destroyAllWindows()
