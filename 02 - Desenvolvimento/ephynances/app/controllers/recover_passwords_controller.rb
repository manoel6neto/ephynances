# encoding: UTF-8
class RecoverPasswordsController < ApplicationController
  before_action :set_recover_password, only: [:show, :edit, :update, :destroy]

  # GET /recover_passwords
  # GET /recover_passwords.json
  def index
    @recover_passwords = RecoverPassword.all
  end

  # GET /recover_passwords/1
  # GET /recover_passwords/1.json
  def show
  end

  # GET /recover_passwords/new
  def new
    @recover_password = RecoverPassword.new
  end

  # GET /recover_passwords/1/edit
  def edit
  end

  # POST /recover_passwords
  # POST /recover_passwords.json
  def create
    @recover_password = RecoverPassword.new(recover_password_params)

    respond_to do |format|
      if @recover_password.save
        format.html { redirect_to @recover_password, notice: 'Recover password was successfully created.' }
        format.json { render :show, status: :created, location: @recover_password }
      else
        format.html { render :new }
        format.json { render json: @recover_password.errors, status: :unprocessable_entity }
      end
    end
  end

  # PATCH/PUT /recover_passwords/1
  # PATCH/PUT /recover_passwords/1.json
  def update
    respond_to do |format|
      if @recover_password.update(recover_password_params)
        format.html { redirect_to @recover_password, notice: 'Recover password was successfully updated.' }
        format.json { render :show, status: :ok, location: @recover_password }
      else
        format.html { render :edit }
        format.json { render json: @recover_password.errors, status: :unprocessable_entity }
      end
    end
  end

  # DELETE /recover_passwords/1
  # DELETE /recover_passwords/1.json
  def destroy
    @recover_password.destroy
    respond_to do |format|
      format.html { redirect_to recover_passwords_url, notice: 'Recover password was successfully destroyed.' }
      format.json { head :no_content }
    end
  end

  private
    # Use callbacks to share common setup or constraints between actions.
    def set_recover_password
      @recover_password = RecoverPassword.find(params[:id])
    end

    # Never trust parameters from the scary internet, only allow the white list through.
    def recover_password_params
      params.require(:recover_password).permit(:token, :validity)
    end
end
