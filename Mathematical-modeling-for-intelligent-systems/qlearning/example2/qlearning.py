# Author: Skander Marnissi 

import gym
import os
os.system("clear")


env = gym.make("Taxi-v3").env

env.render()

"""
env.reset()
env.render()
"""

print("Action Space {}".format(env.action_space))
print("State Space {}".format(env.observation_space))

# (taxi row, taxi column, passenger index, destination index)
state = env.encode(3, 1, 2, 0)
print("State:", state)

env.s = state
env.render()

print(env.P[328])

print("--------------------------------------------------------------------------------------")

"""
solve without q-learning
"""
"""
# set environment to illustration's state
env.s = 328

epochs = 0
penalties, reward = 0, 0

# for animation
frames = []

done = False
while not done:
    action = env.action_space.sample()
    state, reward, done, info = env.step(action)

    if reward == -10:
        penalties += 1

    # Put each rendered frame into dict for animation
    frames.append({
        'frame': env.render(mode='ansi'),
        'state': state,
        'action': action,
        'reward': reward
    })

    epochs += 1

print("Timesteps taken: {}".format(epochs))
print("Penalties incurred: {}".format(penalties))

# from IPython.display import clear_output
# from time import sleep
"""


def print_frames(frames):
    for i, frame in enumerate(frames):
        os.system("clear")
        clear_output(wait=True)
        print(f"Episode: {frame['episode']}")
        print(frame['frame'])
        print(f"Timestep: {i + 1}")
        print(f"State: {frame['state']}")
        print(f"Action: {frame['action']}")
        print(f"Reward: {frame['reward']}")
        sleep(.1)


"""
print_frames(frames)
"""

"""
solve with q-learning
"""
import numpy as np

q_table = np.zeros([env.observation_space.n, env.action_space.n])

"""Training the agent"""

import random
from IPython.display import clear_output
from time import sleep

# Hyperparameters
alpha = 0.1
gamma = 0.6
epsilon = 0.1

# For plotting metrics
all_epochs = []
all_penalties = []

for i in range(1, 100001):
    state = env.reset()

    epochs, penalties, reward, = 0, 0, 0
    done = False

    while not done:
        if random.uniform(0, 1) < epsilon:
            # Explore action space
            action = env.action_space.sample()
        else:
            # Exploit learned values
            action = np.argmax(q_table[state])

        next_state, reward, done, info = env.step(action)

        old_value = q_table[state, action]
        next_max = np.max(q_table[next_state])

        new_value = (1 - alpha) * old_value + alpha * (reward + gamma * next_max)
        q_table[state, action] = new_value

        if reward == -10:
            penalties += 1

        state = next_state
        epochs += 1

    if i % 100 == 0:
        clear_output(wait=True)
        print(f"Episode: {i}")

print("Training finished.\n")

print("Q[S=328]:")
print(q_table[328])

"""
Evaluate agent's performance after Q-learning
"""

total_epochs, total_penalties = 0, 0
episodes = 100
episode_animations = 10

for i in range(episodes):
    state = env.reset()
    epochs, penalties, reward = 0, 0, 0

    done = False

    """Animation"""
    if i < episode_animations:
        frames = []

    while not done:
        action = np.argmax(q_table[state])
        state, reward, done, info = env.step(action)

        if reward == -10:
            penalties += 1

        epochs += 1

        """Animation"""
        if i < episode_animations:
            frames.append({
                'episode': i + 1,
                'frame': env.render(mode='ansi'),
                'state': state,
                'action': action,
                'reward': reward
            })

    total_penalties += penalties
    total_epochs += epochs

    """Animation"""
    if i < episode_animations:
        print_frames(frames)

print(f"Results after {episodes} episodes:")
print(f"Average timesteps per episode: {total_epochs / episodes}")
print(f"Average penalties per episode: {total_penalties / episodes}")






































