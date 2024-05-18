#Aleksa Cup
#SV 27/2022
#Verzija 1.0
#Datum: 22.05.2023.
#Aplikacija Othello(Reversi)

class Reversi:
    def __init__(self):
        self.board = [[' ' for _ in range(8)] for _ in range(8)]
        self.board[3][3] = 'W'
        self.board[3][4] = 'B'
        self.board[4][3] = 'B'
        self.board[4][4] = 'W'
        self.player = 'B'  # prvo igra crni
        self.formattted_valid_moves = []
        self.directions = [(0, 1), (1, 0), (0, -1), (-1, 0), (1, 1), (-1, -1), (1, -1), (-1, 1)]
        self.weights = [[120, -20, 20, 5, 5, 20, -20, 120],
                        [-20, -40, -5, -5, -5, -5, -40, -20],
                        [20, -5, 15, 3, 3, 15, -5, 20],
                        [5, -5, 3, 3, 3, 3, -5, 5],
                        [5, -5, 3, 3, 3, 3, -5, 5],
                        [20, -5, 15, 3, 3, 15, -5, 20],
                        [-20, -40, -5, -5, -5, -5, -40, -20],
                        [120, -20, 20, 5, 5, 20, -20, 120]]

    def valid_moves(self):
        valid_moves = []
        for x in range(8):
            for y in range(8):
                if self.board[x][y] == ' ' and self.valid_move(x, y):
                    valid_moves.append((x, y))
        return valid_moves

    def valid_move(self, x, y):
        for dx, dy in self.directions:
            nx, ny = x + dx, y + dy
            if self.valid_direction(nx, ny, dx, dy):
                return True
        return False

    def valid_direction(self, nx, ny, dx, dy):
        if nx < 0 or nx > 7 or ny < 0 or ny > 7 or self.board[nx][ny] != ('W' if self.player == 'B' else 'B'):
            return False
        while 0 <= nx <= 7 and 0 <= ny <= 7:
            if self.board[nx][ny] == self.player:
                return True
            elif self.board[nx][ny] == ' ':
                return False
            nx += dx
            ny += dy
        return False

    def make_move(self, x, y):
        if self.valid_move(x, y):
            self.board[x][y] = self.player
            flipped_positions = self.flip_disks(x, y)
            self.switch_player()
            return flipped_positions   #ako je validan potez flipuje diskove
        else:
            return []   #nije validan potez

    def undo_move(self, x, y, flipped_positions):
        self.board[x][y] = ' '
        for pos in flipped_positions:
            self.board[pos[0]][pos[1]] = 'W' if self.player == 'B' else 'B'
        self.switch_player()


    def flip_disks(self, x, y):
        flipped_positions = []
        for dx, dy in self.directions:
            if self.valid_direction(x + dx, y + dy, dx, dy):
                nx, ny = x + dx, y + dy
                while self.board[nx][ny] != self.player:
                    self.board[nx][ny] = self.player
                    flipped_positions.append((nx, ny))
                    nx += dx
                    ny += dy
        return flipped_positions

    def count_disks(self):
        return sum(row.count('B') for row in self.board), sum(row.count('W') for row in self.board)

    def switch_player(self):
        self.player = 'W' if self.player == 'B' else 'B'

    


    def game_over(self):
        current_player = self.player
        if len(self.valid_moves()) == 0:
            self.switch_player()
            if len(self.valid_moves()) == 0:
                self.switch_player()  
                return True
        return False
    

    #prikaz table
    def display_board(self):
        print("\n")
        print("   ", end='')
        for i in range(8):
            print(" ", i, end=' ')
        print()
        print("  ", "+---"*8+"+")
        for i in range(8):
            print(chr(65 + i), end='  ')
            for j in range(8):
                print("|", self.board[i][j], end=' ')
            print("|")
            print("  ", "+---"*8+"+")
        print("\n")


    #prikaz validnih poteza
    def display_valid_moves(self):
        valid_moves = self.valid_moves()
        self.formattted_valid_moves = [chr(65 + move[0]) + str(move[1]) for move in valid_moves]
        print("Validni potezi za igraca ", self.player, ": ", self.formattted_valid_moves)



    def evaluate_board(self):
        score = 0
        for i in range(8):
            for j in range(8):
                if self.board[i][j] == 'B':
                    score += self.weights[i][j]
                elif self.board[i][j] == 'W':
                    score -= self.weights[i][j]
        return score

    def best_move(self, depth):
        max_eval = float('-inf')
        validMoves = []
        best_move = None
        for move in self.valid_moves():
            validMoves.append(move)
            flipped_positions = self.make_move(*move)
            eval = self.minimax(depth - 1, float('-inf'), float('inf'), True)
            self.undo_move(*move, flipped_positions)
            if eval > max_eval:
                max_eval = eval
                best_move = move
        self.player = 'W'
        if(best_move == None and len(validMoves) != 0):
            best_move = validMoves[0]
        return best_move

    def minimax(self, depth, alpha, beta, maximizing_player):
        if depth == 0 or self.game_over():
            count_B, count_W = self.count_disks()
            if (self.player == 'B' and count_B > count_W) or (self.player == 'W' and count_W > count_B):
                return float('inf') if maximizing_player else float('-inf')
            elif (self.player == 'B' and count_B < count_W) or (self.player == 'W' and count_W < count_B):
                return float('-inf') if maximizing_player else float('inf')
            else:
                return 0 

        if maximizing_player:
            max_eval = float('-inf')
            for move in self.valid_moves():
                flipped_positions = self.make_move(*move)
                eval = self.minimax(depth - 1, alpha, beta, False)
                self.undo_move(*move, flipped_positions)
                max_eval = max(max_eval, eval)
                alpha = max(alpha, eval)
                if beta <= alpha:
                    break
            return max_eval
        else:
            min_eval = float('inf')
            for move in self.valid_moves():
                flipped_positions = self.make_move(*move)
                eval = self.minimax(depth - 1, alpha, beta, True)
                self.undo_move(*move, flipped_positions)
                min_eval = min(min_eval, eval)
                beta = min(beta, eval)
                if beta <= alpha:
                    break
            return min_eval


def main():
    reversi = Reversi()
    movehistoryblack = []
    movehistorywhite = []
    while not reversi.game_over():
        reversi.display_board()
        if reversi.player == 'B':
            if(len(reversi.valid_moves()) !=0):
                print("Crni je na potezu.")
                reversi.display_valid_moves()
                move_input = (input("Odigrajte potez (npr 'E3'): ")).upper()
                while(True):
                    if(move_input in reversi.formattted_valid_moves):
                        break
                    else:
                        print("Potez koji ste uneli nije validan. Pokusajte ponovo. ")
                        move_input = (input("Odigrajte potez (npr 'E3'): ")).upper()
                move = (ord(move_input[0].upper()) - 65, int(move_input[1]))
                movehistoryblack.append(move_input)
                print("Istorija poteza crnog ", movehistoryblack)
                reversi.make_move(*move)
            else:
                print("Crni nema vise poteza.")
        else:
            print("Beli(AI) je na potezu.")
            reversi.display_valid_moves()
            boardcopy = board_copy(reversi.board)
            move = reversi.best_move(4)  # potez belog igraca
            reversi.board = board_copy(boardcopy)
            if move is not None:
                movehistorywhite.append(chr(65 + move[0]) + str(move[1]))
                print("Istorija poteza belog ",movehistorywhite)
                print(f"\nBeli igrac (AI) je odigrao potez {chr(ord('A') + move[0])}{move[1]}")
                reversi.make_move(*move)
            else:
                print("Beli nema vise poteza.")
    reversi.display_board()
    print("Kraj igre. Rezultat: B - {}, W - {}".format(*reversi.count_disks()))

def board_copy(board):
    rows = len(board)
    cols = len(board[0]) if rows > 0 else 0

    board_copy = [[None] * cols for _ in range(rows)]

    for i in range(rows):
        for j in range(cols):
            board_copy[i][j] = board[i][j]

    return board_copy
 
if __name__ == "__main__":
    main()
