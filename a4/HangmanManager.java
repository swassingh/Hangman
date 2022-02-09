// Swastik Singh
// 02/01/2022
// Omar Ibrahim
// CSE 143 EC

import java.util.*;

// Hangman is a game that has inspired many others. It is a game where
// the first person chooses a word before the game starts and grants
// guesses. This word has to be chosen prior and it can not change.
// In an EVIL! game of hangman, the computer delays picking a specific 
// secret word until it is forced to do so. As a result, the computer 
// is always considering a set of words that could be the secret word. 
// In order to fool the user into thinking it is playing fairly, the 
// computer only considers words with the same letter pattern.


public class HangmanManager{

   private int guessesLeft;
   private Set<Character> guessChar;
   private String displayed;
   private  Set<String> considered;
      
   // Constructor for the HangmanManager Class
   // Recieves a dictionary of words, target word length, and a maximum 
   // number of wrong guesses the player is allowed to guess.
   // Uses the parameters given to initialize state of the game
   // Throws IllegalArgumentException if length is < 1 or max is < 0
   // Assumes Collection contains only non-empty strings of only lowercase letters
   public HangmanManager(Collection<String> dictionary, int length, int max){
      if (length < 1 || max < 0){
         throw new IllegalArgumentException();
      }
      
      guessesLeft = max;
      guessChar = new TreeSet<>();
      displayed = "-";
      considered = new TreeSet<>();      
      
      for (String wordsOfLength : dictionary){
         if (wordsOfLength.length() == length){
            considered.add(wordsOfLength);
         }
      }
            
      for (int i = 0; i < length - 1; i++){
         displayed += " -";
      }        
   }
   
   // method returns the current set of words considered by HaangmanManager
   public Set<String> words(){
      return considered;
   }
   
   // returns the number of guesses the user has left
   public int guessesLeft(){
      return guessesLeft;
   }
   
   // returns the letters guessed by the user
   public Set<Character> guesses(){
      return guessChar;
   }
   
   // Method returns the current pattern to be displayed for the game
   // takes into account the guesses that have been made. Unknown letters
   // are displayed as dashes (-) and there will be a space in between 
   // letters. (no leading or trailing spaces)
   // Throws IllegalStateException if set of words is empty.
   public String pattern(){
      if (considered.isEmpty()){
         throw new IllegalStateException();
      }
      
      return displayed;
   }
   
   // returns the number of times the guesed letter is in the new pattern
   // of words
   // records the guess made by user, uses the guess to determine what set 
   // of words to use after this call of record, update the guesses left
   // throws IllegalStateException if num guesses is < 1 or if set of words
   // is empty
   // throws IllegalArgumentException if character guessed has already been
   // guessed before
   // Assumes char guess only recieves lowercase letters
   public int record(char guess){
      if (considered.isEmpty() || guessesLeft < 1){
         throw new IllegalStateException();
      } else if (!considered.isEmpty() && guessChar.contains(guess)){
         throw new IllegalArgumentException();
      }
      
      guessChar.add(guess);
      
      Map<String, Set<String>> mappedFriends = new TreeMap<>();
      for (String word : considered){
         String patternChecker = generatePattern(word, guess);
         Set<String> setWords = new TreeSet<>();
         if (!mappedFriends.containsKey(patternChecker)){
            mappedFriends.put(patternChecker, setWords);
         }
         mappedFriends.get(patternChecker).add(word);
      }
      
      int max = 0;
      for (String currPattern : mappedFriends.keySet()){
         if (!mappedFriends.isEmpty() && mappedFriends.get(currPattern).size() > max){
            considered.clear();
            considered.addAll(mappedFriends.get(currPattern));
            displayed = currPattern;
            max = mappedFriends.get(currPattern).size();
         }
      }
      return numOccurs(guess);
   }
   
   // Helper method for record(guess)
   // takes a string and char as parameters
   // returns a pattern of letters in the form of a word
   // determines the best pattern of letters to be Evil
   // in Hangman
   private String generatePattern(String word, char guess){
      String pattern = "";
      for (int i = 0; i < word.length(); i++){
         if (word.charAt(i) != guess){
            pattern += displayed.substring(2 * i, (2 * i) + 1);
         } else{
            pattern += guess;
         }
         if (i != word.length() - 1){
            pattern += " ";
         }
      }
      
      return pattern;
   }
   
   // takes a char as a param
   // returns an int
   // helper method for record
   // determines the number of time something occurs
   // in the word determined
   private int numOccurs(char guess){
      int occurs = 0;
      for (int i = 0; i < displayed.length(); i++){
         if (guess == displayed.charAt(i)){
            occurs++;
         }
      }
      if (occurs == 0){
         guessesLeft--;
      }
      
      return occurs;
   }
}