# encoding: utf-8
namespace :app do
  desc 'Encrypt ao passwords in plain text on database'
  task migrate_passwords: :environment do

    User.find_each do |user|
      puts "Migrate user ##{user.id} #{user.name}"
      unencrypted_password = user.attributes['password_digest']
      user.password = unencrypted_password
      user.password_confirmation = unencrypted_password
      user.save!
    end

  end
end
