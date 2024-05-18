import matplotlib.pyplot as plt
import numpy as np
from matplotlib import font_manager
from matplotlib import colors as mcolors

# Make a list of vivid colors
colors = list(mcolors.TABLEAU_COLORS.values())[:50]

def draw_chart(data):
    # We have the same number of rows as machines
    nb_row = len(data.keys())

    # We want to place the bars in the middle of the row
    # So we need to place the bars at 0.5, 1.5, 2.5, ...
    pos = np.arange(0.5, nb_row * 0.5 + 0.5, 0.5)

    # Create a figure
    fig = plt.figure(figsize=(20, 8))
    ax = fig.add_subplot(111)

    # For each machine
    index = 0 # We start with machine 0
    max_len = [] # We will use this to set the x axis limit

    # For each Machine we have a list of operations
    for machine, operations in sorted(data.items()):
        # For each operation
        for op in operations:
            # Job index is the first number in the operation label and the operation index is the second number
            job_index, operation_index = int(op[2].split('-')[0]), int(op[2].split('-')[1])
            # add the operation length to the max_len list
            max_len.append(op[1])
            # Get the color for this job; We have 50 colors in the colors list so we use the modulo operator to get the color
            color=colors[job_index%len(colors)]

            # Draw the bar
            rect = ax.barh((index * 0.5) + 0.5, op[1] - op[0], left=op[0], height=0.3, align='center',
                           edgecolor=color, color=color, alpha=0.8)

            # adding label
            width = int(rect[0].get_width())
            Str = "OP_{}".format(op[2])
            xloc = op[0] + 0.50 * width
            clr = 'black'
            align = 'center'

            # The y position is the middle of the bar
            yloc = rect[0].get_y() + rect[0].get_height() / 2.0

            # Add the label
            ax.text(xloc, yloc, Str, horizontalalignment=align,
                            verticalalignment='center', color=clr, weight='bold',
                            clip_on=True, fontsize=10)
        # Go to the next machine
        index += 1

    # Set the y axis limits
    ax.set_ylim(ymin=-0.1, ymax=nb_row * 0.5 + 0.5)
    ax.grid(color='gray', linestyle=':')
    # Set the x axis limits; The max_len list contains the end time of each operation
    ax.set_xlim(0, max(10, max(max_len)))

    # Set the x axis labels
    labelsx = ax.get_xticklabels()
    plt.setp(labelsx, rotation=0, fontsize=10)

    # Set the y axis labels
    locsy, labelsy = plt.yticks(pos, data.keys())
    plt.setp(labelsy, fontsize=14)

    # Add a legend
    font = font_manager.FontProperties(size='small')
    ax.legend(loc=1, prop=font)

    # Flip the y axis so that the machine 0 is at the top
    ax.invert_yaxis()

    plt.title("Flexible Job Shop Solution")
    plt.savefig('gantt.svg')
    plt.show()