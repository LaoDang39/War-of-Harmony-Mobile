module CheatManager
  SEARCH_VAR_ID = 3
  @ohk_hooked = false

  def self.poll
    return unless $game_party && !$game_party.members.empty?
    if Android.cheat_invincible?
      $game_party.members.each { |a| a.hp = a.mhp }
    end
    if Android.cheat_infinite_mp?
      $game_party.members.each { |a| a.mp = a.mmp }
    end
    if Android.cheat_infinite_search?
      $game_variables[SEARCH_VAR_ID] = 999 if $game_variables
    end
    apply_one_hit_kill_hook unless @ohk_hooked
    gold = Android.consume_pending_gold
    $game_party.gain_gold(gold) if gold != 0
  end

  def self.apply_one_hit_kill_hook
    return unless defined?(Game_Enemy)
    return unless Game_Enemy.method_defined?(:make_damage_value)
    @ohk_hooked = true
    Game_Enemy.class_eval do
      alias_method :cheat_make_damage_value, :make_damage_value
      def make_damage_value(user, item)
        cheat_make_damage_value(user, item)
        if user.is_a?(Game_Actor) && Android.cheat_one_hit_kill?
          @result.hp_damage = self.hp
        end
      end
    end
  end
end

module Graphics
  class << self
    alias_method :update_without_cheat, :update
    def update
      CheatManager.poll
      update_without_cheat
    end
  end
end