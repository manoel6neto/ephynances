# encoding: UTF-8
class UserLevelsController < ApplicationController
  before_action :set_user_level, only: [:show, :edit, :update, :destroy]

  # GET /user_levels
  # GET /user_levels.json
  def index
    @user_levels = UserLevel.all
  end

  # GET /user_levels/1
  # GET /user_levels/1.json
  def show
  end

  # GET /user_levels/new
  def new
    @user_level = UserLevel.new
  end

  # GET /user_levels/1/edit
  def edit
  end

  # POST /user_levels
  # POST /user_levels.json
  def create
    @user_level = UserLevel.new(user_level_params)

    respond_to do |format|
      if @user_level.save
        format.html { redirect_to @user_level, notice: 'User level was successfully created.' }
        format.json { render :show, status: :created, location: @user_level }
      else
        format.html { render :new }
        format.json { render json: @user_level.errors, status: :unprocessable_entity }
      end
    end
  end

  # PATCH/PUT /user_levels/1
  # PATCH/PUT /user_levels/1.json
  def update
    respond_to do |format|
      if @user_level.update(user_level_params)
        format.html { redirect_to @user_level, notice: 'User level was successfully updated.' }
        format.json { render :show, status: :ok, location: @user_level }
      else
        format.html { render :edit }
        format.json { render json: @user_level.errors, status: :unprocessable_entity }
      end
    end
  end

  # DELETE /user_levels/1
  # DELETE /user_levels/1.json
  def destroy
    @user_level.destroy
    respond_to do |format|
      format.html { redirect_to user_levels_url, notice: 'User level was successfully destroyed.' }
      format.json { head :no_content }
    end
  end

  private
    # Use callbacks to share common setup or constraints between actions.
    def set_user_level
      @user_level = UserLevel.find(params[:id])
    end

    # Never trust parameters from the scary internet, only allow the white list through.
    def user_level_params
      params.require(:user_level).permit(:levelType)
    end
end
